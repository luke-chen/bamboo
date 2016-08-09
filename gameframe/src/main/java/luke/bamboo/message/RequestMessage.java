package luke.bamboo.message;

public class RequestMessage extends Message {
	public RequestMessage() {
	}
	
	public RequestMessage(short msgId) {
		this.id = msgId;
	}
}
