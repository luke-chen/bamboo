package luke.bamboo.netty;

import io.netty.channel.Channel;
import luke.bamboo.logic.service.GameLogicInterface;
import luke.bamboo.message.ResponseMessage;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class GameContext {
	
	private static ArrayList<GameLogicInterface> orderedServices = new ArrayList<GameLogicInterface>();
	
	private static ConcurrentHashMap<Long, Channel> allChannels = new ConcurrentHashMap<Long, Channel>();
	
	public static boolean pushMessage(Long id, ResponseMessage resp) {
		Channel channel = allChannels.get(id);
		if(channel != null && channel.isActive() && channel.isWritable()) {
			channel.writeAndFlush(resp);
			return true;
		}
		return false;
	}
	
	public static void addChannel(Long id, Channel channel) {
		allChannels.put(id, channel);
	}
	
	public static Channel removeChannel(Long id) {
		return allChannels.remove(id);
	}
	
	public static void addGameService(GameLogicInterface gameService) {
		orderedServices.add(gameService);
	}
	
	public static void serviceOnline(long playerId) {
		for(GameLogicInterface s : orderedServices) {
			s.online(playerId);
		}
	}
	
	public static void serviceOffline(long playerId) {
		for(int i = orderedServices.size() - 1; i >= 0; i --) {
			orderedServices.get(i).offline(playerId);
		}
	}
	
	public static void serviceLogin(long playerId) {
		for(GameLogicInterface s : orderedServices) {
			s.login(playerId);
		}
	}
	
	public static void serviceLogout(long playerId) {
		for(int i = orderedServices.size() - 1; i >= 0; i --) {
			orderedServices.get(i).logout(playerId);
		}
	}
}
