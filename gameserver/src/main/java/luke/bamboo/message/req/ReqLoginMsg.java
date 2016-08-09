package luke.bamboo.message.req;

import luke.bamboo.annotation.RequestID;
import luke.bamboo.message.id.MessageID;

@RequestID(MessageID.REQ_LOGIN)
public class ReqLoginMsg {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
