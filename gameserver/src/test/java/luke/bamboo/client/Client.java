package luke.bamboo.client;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import luke.bamboo.common.util.JsonUtil;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.resp.RspLoginMsg;

public class Client extends Thread {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    // 单个正在写的上行报文缓冲
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private DataOutputStream dos = new DataOutputStream(baos);

    /**
     * 已包装好的上行报文缓冲
     */
    private ArrayList<byte[]> outBuffer = new ArrayList<byte[]>();

    /**
     * 下行报文缓冲，在读取缓冲时阻塞网络线程
     */
    @SuppressWarnings("unused")
	private DataInputStream disBuffer = null;

    /**
     * 报文解析器
     */
    private ClientParse parse;

    /**
     * @param _ip
     * @param _port
     * @param _parse
     * @throws Exception
     */
    public Client(ClientParse _parse) {
        socket = new Socket();
        parse = _parse;
    }

    /**
     * 开始网络连接
     * 
     * @param timeout
     *            连接超时
     * @param ip
     *            IP地址
     * @param port
     *            端口号
     * @param soTimeout
     *            读取数据超时
     * @param soKeepAlive
     *            监视TCP是否有效
     * @throws Exception
     */
    public void connection(int timeout, String ip, int port, int soTimeout,
            boolean soKeepAlive) throws Exception {
        if (socket == null)
            throw new NullPointerException("scoket of client is null");

        // 是否及时发送
        socket.setTcpNoDelay(false);
        socket.setTrafficClass(0x2 | 0x4 | 0x8 | 0x10);

        // socket等待读取数据的时间
        socket.setSoTimeout(soTimeout);

        // socketTCP底层监视Socket连接是否有效
        // 即隔一段时间发送一个数据包给远程端，远程端口在收到数据包之后回应
        socket.setKeepAlive(soKeepAlive);
        
        String[] ips = ip.split("\\.");
		int[] ipv = new int[] {Integer.valueOf(ips[0]), Integer.valueOf(ips[1]),
				Integer.valueOf(ips[2]), Integer.valueOf(ips[3]) };
		byte[] ipb = new byte[4];
		for(int i=0; i<ipb.length; i++) {
			ipb[i] = (byte)ipv[i];
		}
        InetAddress addr = InetAddress.getByAddress(ipb);
        InetSocketAddress socketAddr = new InetSocketAddress(addr, port);
        socket.connect(socketAddr, timeout);
        is = socket.getInputStream();
        os = socket.getOutputStream();
        this.start();
    }

    /**
     * 关闭网络连接
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (is != null)
            is.close();
        if (os != null)
            os.close();
        if (socket != null)
            socket.close();
    }

    /**
     * 获得缓冲
     * 
     * @return
     * @throws IOException
     */
    public DataOutputStream getOutBuffer() throws IOException {
        baos.write(new byte[4]);
        return dos;
    }

    /**
     * 包装报文
     */
    public void packageIt() {
        byte[] content = baos.toByteArray();
        int len = (content.length - 4);
        content[0] = (byte) (len >> 24 & 0x000000FF);
        content[1] = (byte) (len >> 16 & 0x000000FF);
        content[2] = (byte) (len >> 8 & 0x000000FF);
        content[3] = (byte) (len & 0x000000FF);
        outBuffer.add(content); // 目前只支持一次上行一条报文
        baos.reset();
    }

    /**
     * 立刻发送并清空缓冲
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        for (int i = 0; i < outBuffer.size(); i++) {
            byte[] t = outBuffer.get(i);
            os.write(t);
        }
        os.flush();
        outBuffer.clear();
    }

    @Override
    public void run() {

        while (true) {
            try {
                // 读取数据
            	byte[] data = readOfBlock();
            	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
            	short msgId = dis.readShort();
            	
            	ResponseMessage resp = new ResponseMessage();
            	resp.setId(msgId);
				if (data.length > 2) {
					byte[] b = new byte[data.length - 2];
					dis.readFully(b);
					String json = new String(b, "utf-8");
					resp.setData(JsonUtil.toObject(json));
				}
            	 // 解析数据
                parse.parse(resp);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // 网络出错直接退出
                return;
            }
        }
    }

    private byte[] readOfBlock() throws IOException {
        ////////////////// 读取报文长度 ///////////////////////
        int length = 0;
        int lenCount = 0;
        int error = 0;
        int maxError = 3;
        do {
            int value = 0;
            //System.out.println("start read");
            if ((value = is.read()) == -1) {
                error++;
                System.err.println("报文长度为-1,没有收到数据");
                continue;
            } else {
                if (lenCount == 0) {
                    length = value & 0xFF;
                } else if (lenCount == 1) {
                    length = (length << 8) | (value & 0xFF);
                } else if (lenCount == 2) {
                    length = (length << 8) | (value & 0xFF);
                } else if (lenCount == 3) {
                    length = (length << 8) | (value & 0xFF);
                }
                lenCount++;
            }
        } while (lenCount < 4 && error < maxError);

        // ////////////////// 报文长度合法性检验 ///////////////////////
        if (error >= maxError) {
            throw new IOException("无法读取报文长度");
        }

        if (length > 90000 || length <= 0) {
            throw new IOException("报文长度不合法:" + length);
        }

        //////////////////// 读取报文内容 /////////////////////////////
        byte[] data = new byte[length]; // 数据缓冲
        int retry = 0; // 重试次数
        int maxRetry = 9; // 最大重试次数
        int off = 0;
        do {
            off += is.read(data, off, length - off);
        } while (off < length && retry++ < maxRetry);

        // 报文内容读取完毕
        if (off == length) {
            return data;
        } else {
            throw new IOException("达到最大重试次数,当前读取长度=" + off + ",总长度=" + length);
        }
    }

    public Socket getSocket() {
        return socket;
    }
    
    public void writeAndFlush(RequestMessage req) throws IOException {
    	System.out.println("writeAndFlush:"+JsonUtil.toJsonString(req.getData()));
        DataOutputStream dos = getOutBuffer();
        dos.writeShort(req.getId());
        byte[] data = JsonUtil.toByteArray(req.getData());
        dos.write(data, 0, data.length);
        packageIt();
        flush();
        System.out.println("flush!");
    }

    // public static void main(String[] str)
    // {
    // try
    // {
    // new ClientUI();
    // // for (int i = 0; i < 1; i++)
    // // {
    // // Client c = new Client();
    // // c.sleep(10);
    // // }
    // }
    // catch (Exception e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
}
