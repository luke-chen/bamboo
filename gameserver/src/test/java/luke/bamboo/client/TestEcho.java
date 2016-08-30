package luke.bamboo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqEchoMsg;

public class TestEcho extends Thread implements ClientParse {
	Client client;
	String ip;
	int port;
	TimerCounter t;
	int loopNum;
	int intval;

	public TestEcho(String ip, int port, TimerCounter t, int loopNum, int intval) throws Exception {
		this.ip = ip;
		this.port = port;
		this.t = t;
		this.loopNum = loopNum;
		this.intval = intval;
	}

	public void run() {
		try {
			System.out.println("connect ip:"+ip+" port:"+port);
			client = new Client(this);
			client.connection(5000, ip, port, 60 * 1000, true);
			for (int i = 0; i < loopNum; i++) {
				sendEcho();
				client.readAndDecodePackage();
				sleep(intval);
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
		//System.out.println("parse msgID=" + msgID + " t:" + new Date(serverT * 1000) + "t:" + t.getAverage());

		switch (msgID) {
		case MessageID.RSP_ECHO:
			//System.out.println("echo data:" + ((JSONObject) resp.getData()).getString("data"));
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
		//System.out.println("sendEcho");
		ReqEchoMsg data = new ReqEchoMsg();
		data.setData("this is a test echo message, please ignore!");
		client.writeAndFlush(MessageID.REQ_ECHO, data);
		t.addStartTime();
	}

	static ArrayList<TimerCounter> list = new ArrayList<TimerCounter>();

	public static void main(String[] str) throws Exception {
		String host = "192.168.137.11";
		int port = 8610;
		System.out.println("connect host:"+host+":"+port);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("input client number:");
		int clientNum = Integer.parseInt(br.readLine().trim());
		System.out.print("input loop number:");
		int loopNum = Integer.parseInt(br.readLine().trim());
		System.out.print("intval (ms):");
		int intval = Integer.parseInt(br.readLine().trim());
		System.out.println("clientNum:"+clientNum+" loopNum:"+loopNum);
		ArrayList<TestEcho> tests = new ArrayList<TestEcho>();
		for (int i = 0; i < clientNum; i++) {
			Thread.sleep(20);
			TimerCounter t = new TimerCounter();
			list.add(t);
			TestEcho test = new TestEcho(host, port, t, loopNum, intval);
//			TestEcho test = new TestEcho("127.0.0.1", 8610, t, loopNum, intval);
			test.start();
			tests.add(test);
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
