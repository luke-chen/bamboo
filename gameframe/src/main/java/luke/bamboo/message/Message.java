package luke.bamboo.message;

/**
 * Message
 * @author luke
 */
public class Message {
	protected short id;
	protected Object data;
	
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
