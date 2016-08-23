package luke.bamboo.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqEchoMsg;

public class TestEcho extends Thread  implements ClientParse {
	Client client;
	int[] ip;
	int port;
	TimerCounter t;
	
	public TestEcho(int[] ip, int port, TimerCounter t) throws Exception {
		this.ip = ip;
		this.port = port;
		this.t = t;
	}
    
	public void run() {
		try {
			client = new Client(this);
			client.connection(5000, "127.0.0.1", 8610, 60*1000, true);
			for (int i = 0; i < 100; i++) {
				sendEcho();
				client.readAndDecodePackage();
				sleep(20);
			}
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
	public void parse(ResponseMessage resp) throws IOException {
		// 报文解析
			t.getSpendTime();
			short msgID = resp.getId();
			long serverT = resp.getT();
			System.out.println("parse msgID=" + msgID + " t:" + new Date(serverT * 1000) + "t:" + t.getAverage());

			switch (msgID) {
			case MessageID.RSP_ECHO:
				System.out.println("echo data:" + ((JSONObject) resp.getData()).getString("data"));
				break;
			case MessageID.RSP_UNKNOWN:
				System.out.println("unknow");
				break;
			case MessageID.RSP_HEART_BEAT:
				System.out.println("heart beat ok");
				break;
			case MessageID.RSP_TOKEN_INVALID:
				System.out.println("invalid token");
				break;
			}
	}

    public void sendEcho() throws IOException {
    	System.out.println("sendEcho");
    	ReqEchoMsg data = new ReqEchoMsg();
    	data.setData("test echo");
    	client.writeAndFlush(MessageID.REQ_ECHO, data);
    	t.addStartTime();
    }
    
    public static void main(String[] str) throws Exception {
		ArrayList<TimerCounter> list = new ArrayList<TimerCounter>();
		ArrayList<TestEcho> tests = new ArrayList<TestEcho>();
		final int num = 100;
		for (int i = 0; i < num; i++) {
			Thread.sleep(400);
			TimerCounter t = new TimerCounter();
			list.add(t);
			TestEcho test = new TestEcho(new int[] { 127, 0, 0, 1 }, 8608, t);
			test.start();
			tests.add(test);
		}
		
		for(TestEcho t : tests)
			t.join();
		TimerCounter.printReport(list);
	}
}
