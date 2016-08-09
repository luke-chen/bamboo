package luke.bamboo.gate.vo.response;

import luke.bamboo.data.domain.GameServerInfo;

public class LoginSucessInfo {
	String accountName;
	
	String token;
	
	/**
	 * 游戏服务器信息
	 */
	GameServerInfo gameServerInfo;
	
	public LoginSucessInfo(String accountName, String token, GameServerInfo gameServerInfo) {
		this.accountName = accountName;
		this.token = token;
		this.gameServerInfo = gameServerInfo;
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public GameServerInfo getGameServerInfo() {
		return gameServerInfo;
	}

	public void setGameServerInfo(GameServerInfo gameServerInfo) {
		this.gameServerInfo = gameServerInfo;
	}
}
