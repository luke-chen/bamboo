package luke.bamboo.message;

import java.util.Calendar;

public class ResponseMessage extends Message {
	
	private long t = Calendar.getInstance().getTimeInMillis()/1000;
	
	public void set(short id, Object data) {
		this.id = id;
		this.data = data;
	}

	public long getT() {
		return t;
	}

	public void setT(long serverTime) {
		this.t = serverTime;
	}
}
