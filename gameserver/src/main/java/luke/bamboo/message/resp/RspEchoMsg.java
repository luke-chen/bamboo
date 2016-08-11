package luke.bamboo.message.resp;

import luke.bamboo.annotation.RequestID;
import luke.bamboo.message.id.MessageID;

@RequestID(MessageID.REQ_ECHO)
public class RspEchoMsg {
	String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
