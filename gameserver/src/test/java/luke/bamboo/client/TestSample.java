package luke.bamboo.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.game.wego.gate.vo.response.Success;

import luke.bamboo.common.util.HttpClientUtils;
import luke.bamboo.common.util.JsonUtil;
import luke.bamboo.common.util.RspCode;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqEchoMsg;
import luke.bamboo.message.resp.RspLoginMsg;

public class TestSample extends Thread  implements ClientParse {
	Client client;
	int[] ip;
	int port;
	
	public TestSample(int[] ip, int port) throws Exception {
		this.ip = ip;
		this.port = port;
	}
    
	public static void main(String[] str) throws Exception {
		TestSample t = new TestSample(new int[] { 127, 0, 0, 1 }, 8608);
		// TestNetty t = new TestNetty(new int[] {221,6,14,182}, 8601);
		t.start();
	}
    
	public void run() {
		try {
			// 连接gate服务器
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("accountName", "a1b6c393-38ba-4e89-bf73-6037987a4e16"));
////			params.add(new BasicNameValuePair("accountName", ""));
//			params.add(new BasicNameValuePair("password", "password"));
//			params.add(new BasicNameValuePair("randomName", "0"));
//			RspCode rspCode= new RspCode();
//			Success success = HttpClientUtils.fetchByPost("http://221.6.14.182:8011/wego-gate/loginAndRegister", params, Success.class, rspCode);
//			System.out.println("**登录gate成功**");
//			
//			JSONObject account = (JSONObject)success.getData();
//			String token = account.getString("token");
//			JSONObject server = (JSONObject)account.get("gameServerInfo");
//			String ip = server.getString("ip");
//			int port = server.getInteger("port");
//			
//			Thread.sleep(5000);
//			// 连接game服务器
//	    	client = new Client(this);
//			client.connection(5000, ip, port, 60*1000, true);
//			sendLogin(token);
//			for (int i=0; i<1; i++) {
//				sendHeartBeat();
//				Thread.sleep(30000);
//			}
			
			client = new Client(this);
			client.connection(5000, "127.0.0.1", 8610, 60*1000, true);
			for (int i=0; i<2; i++) {
				sendEcho();
				Thread.sleep(30000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void sendTest() throws IOException {
//        发送2个数据包
//        DataOutputStream dos = net.getOutBuffer();
//        dos.writeShort(0x0001);
//        dos.writeUTF(str);
//        net.packageIt();
//        dos = net.getOutBuffer();
//        dos.writeShort(0xF000);
//        dos.writeUTF(str);
//        net.packageIt();
//        net.flush();
        
        // 发送1个数据包和1个片段包
//        DataOutputStream dos = new DataOutputStream(net.getSocket().getOutputStream());
//        dos.writeInt(bigStr.getBytes("utf-8").length+2+2);
//        dos.writeShort(MessageHandler.REQ_LOGIN);
//        dos.writeUTF(bigStr);
//        dos.writeInt(6);
//        dos.flush(); //片段发送
//        try {
//            Thread.currentThread().sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        dos.writeShort(MessageHandler.REQ_FIGHT_ATTACK);
//        dos.writeInt(12345);
//        dos.flush();
    }

    @Override
	public void parse(ResponseMessage resp) throws IOException {
		// 报文解析
		short msgID = resp.getId();
		long t = resp.getT();
		 System.out.println("parse msgID="+msgID+ " t:"+new Date(t*1000)+ "t:"+t);

		switch(msgID) {
			case MessageID.RSP_ECHO:
				System.out.println("echo");
				break;
			case MessageID.RSP_LOGIN_OK:
				System.out.println("login ok");
				break;
			case MessageID.RSP_LOGIN_ERROR:
				System.out.println("login error");
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
			case MessageID.RSP_BUILD_OK:
				JSONObject json = (JSONObject)resp.getData();
				System.out.println("build ok ");
				System.out.println(json.toJSONString());
				break;
			case MessageID.RSP_BUILD_ERROR:
				System.out.println("build error");
				break;
			default:
				System.out.println("unknow id"+msgID);
		}
	}

    public void sendHeartBeat() throws IOException {
    	System.out.println("sendHeartBeat");
    	RequestMessage req = new RequestMessage();
    	req.setId(MessageID.REQ_HEART_BEAT);
    	client.writeAndFlush(req);
    }
    
//    public void sendLogin2(String token) throws IOException {
//    	System.out.println("sendLogin");
//    	RspLoginData data = new RspLoginData();
//    	data.setToken(token);
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_LOGIN);
//    	req.setData(data);
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        System.out.println(JsonUtil.toJsonString(req));
//        byte[] t = JsonUtil.toByteArray(req);
//        dos.write(t, 0, t.length);
//        
//        net.packageIt();
//        net.flush();
//    }
    public void sendEcho() throws IOException {
    	System.out.println("sendEcho");
    	ReqEchoMsg data = new ReqEchoMsg();
    	data.setData("test echo");
    	RequestMessage req = new RequestMessage();
    	req.setId(MessageID.REQ_ECHO);
    	req.setData(data);
    	client.writeAndFlush(req);
    }
    public void sendLogin(String token) throws IOException {
    	System.out.println("sendLogin");
    	RspLoginMsg data = new RspLoginMsg();
    	data.setToken(token);
    	RequestMessage req = new RequestMessage();
    	req.setId(MessageID.REQ_LOGIN);
    	req.setData(data);
    	System.out.println(JsonUtil.toJsonString(req.getData()));
    	
        DataOutputStream dos = client.getOutBuffer();
        dos.writeShort(req.getId());
        byte[] t = JsonUtil.toByteArray(req.getData());
        dos.write(t, 0, t.length);
        client.packageIt();
        client.flush();
    }
    
//    public void sendMakeBuilding2(String token) throws IOException {
//    	System.out.println("sendMakeBuilding");
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_BUILD);
//    	HashMap<String, Object> map = new HashMap<String, Object>();
//    	map.put("baseId", 2002);
//    	map.put("position", "10,20,1");
//    	req.setData(map);
//        DataOutputStream dos = net.getOutBuffer();
//        System.out.println(JsonUtil.toJsonString(req));
//        byte[] t = JsonUtil.toByteArray(req);
//        dos.write(t, 0, t.length);
//        
//        net.packageIt();
//        net.flush();
//    }
    
//    public void sendMakeBuilding(String token) throws IOException {
//    	System.out.println("sendMakeBuilding");
//    	ReqMakeBuildingMsg data = new ReqMakeBuildingMsg();
//    	data.setBaseId(2002);
//    	data.setPosition("10,20,1");
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_BUILD);
//    	req.setData(data);
//    	System.out.println(JsonUtil.toJsonString(data));
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        byte[] t = JsonUtil.toByteArray(data);
//        dos.writeShort(req.getId());
//        dos.write(t, 0, t.length);
//        net.packageIt();
//        net.flush();
//    }
//    
//    public void sendUpBuilding(String token, int buildId) throws IOException {
//    	System.out.println("sendUpBuilding");
//    	ReqUpBuildingMsg data = new ReqUpBuildingMsg();
//    	data.setBuildId(buildId);
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_UP_BUILD);
//    	req.setData(data);
//    	System.out.println(JsonUtil.toJsonString(data));
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        byte[] t = JsonUtil.toByteArray(data);
//        dos.writeShort(req.getId());
//        dos.write(t, 0, t.length);
//        net.packageIt();
//        net.flush();
//    }
//    
//    public void sendAddSoldier() throws IOException {
//    	System.out.println("sendAddSoldier");
//    	ReqMakeSoldierMsg data = new ReqMakeSoldierMsg();
//    	data.setSolderBaseId(100101);
//    	data.setBuildBaseId(0);
//    	data.setBuildId(0);
//    	data.setNum(0);
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_MAKE_GOODS);
//    	req.setData(data);
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        byte[] t = JsonUtil.toByteArray(data);
//        dos.writeShort(req.getId());
//        dos.write(t, 0, t.length);
//        net.packageIt();
//        net.flush();
//    }
//    
//    public void searchBattle() throws IOException {
//    	System.out.println("search battle");
//    	ReqEnterBattleMsg data = new ReqEnterBattleMsg();
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_SEARCH_OPPONENT);
//    	req.setData(data);
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        byte[] t = JsonUtil.toByteArray(data);
//        dos.writeShort(req.getId());
//        dos.write(t, 0, t.length);
//        net.packageIt();
//        net.flush();
//    }
//    
//    public void refresh() throws IOException {
//    	System.out.println("refresh");
//    	ReqRefreshMsg data = new ReqRefreshMsg();
//    	RequestMessage req = new RequestMessage();
//    	req.setId(Message.REQ_REFRSH_INFO);
//    	req.setData(data);
//    	
//        DataOutputStream dos = net.getOutBuffer();
//        byte[] t = JsonUtil.toByteArray(data);
//        dos.writeShort(req.getId());
//        dos.write(t, 0, t.length);
//        net.packageIt();
//        net.flush();
//    }
}
