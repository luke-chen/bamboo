package luke.bamboo.monitor;

import java.util.Date;

public class GameStatus {
	Date time;
	long activeNum;
	long onlineNum;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public long getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(long activeNum) {
		this.activeNum = activeNum;
	}

	public long getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(long onlineNum) {
		this.onlineNum = onlineNum;
	}
}
