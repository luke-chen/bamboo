package luke.bamboo.logic.service;

public interface GameLogicInterface {
	/**
	 * do something when user login
	 */
	void login(long playerId);
	
	/**
	 * do something when user logout
	 */
	void logout(long playerId);
	
	/**
	 * do something when user is online
	 */
	void online(long playerId);
	
	/**
	 * do something when user is offline
	 */
	void offline(long playerId);
}
