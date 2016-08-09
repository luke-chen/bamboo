package luke.bamboo.logic.service.player;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import luke.bamboo.annotation.GameService;
import luke.bamboo.logic.service.GameLogicInterface;

@GameService(order = 1)
public class PlayerLogicService implements GameLogicInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(PlayerLogicService.class);

	@Override
	public void login(long playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout(long playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void online(long playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offline(long playerId) {
		// TODO Auto-generated method stub
		
	}
}
