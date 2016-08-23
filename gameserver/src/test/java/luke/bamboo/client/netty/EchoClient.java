package luke.bamboo.client.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import luke.bamboo.client.TimerCounter;

public class EchoClient extends Thread {
	static ArrayList<TimerCounter> list = new ArrayList<TimerCounter>();
	static int clientNum = 0;
	static int loopNum = 0;
	static int intval = 0;
	String host = "127.0.0.1";
    int port = 8610;
	
    @Override
	public void run(){
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});

			// Start the client.
			Channel ch = b.connect(host, port).channel();
			// Wait until the connection is closed.
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
	
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("input client number:");
        EchoClient.clientNum = Integer.parseInt(br.readLine().trim());
        System.out.print("input loop number:");
        EchoClient.loopNum = Integer.parseInt(br.readLine().trim());
        System.out.print("intval (ms):");
        EchoClient.intval = Integer.parseInt(br.readLine().trim());
        System.out.println("clientNum:"+clientNum+" loopNum:"+loopNum);
        
        for(int i=0; i<clientNum; i++) {
        	new EchoClient().start();
        	//Thread.sleep(10000);
        }
        
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					System.out.println("The JVM Hook is execute");
					TimerCounter.printReport(list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
