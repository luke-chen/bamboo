package luke.bamboo.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import luke.bamboo.common.util.StringUtil;
import luke.bamboo.data.db.mapper.PlayerMapper;
import luke.bamboo.data.domain.Player;
import redis.clients.jedis.JedisPool;

@Service
public class PlayerDataService {

	@Autowired
	private PlayerMapper playerMapper;

	@Autowired
	@Qualifier("myJedisPool")
	private JedisPool jedisPool;

	@Transactional
	public void createPlayer(Player player) {
		playerMapper.addPlayer(player);
	}

	public Player getPlayer(long playerId) {
		return playerMapper.getPlayer(playerId);
	}

	public Player getPlayerByAccount(int accountId) {
		return playerMapper.getPlayerByAccount(accountId);
	}

	public boolean checkPlayerName(String name) {
		return !StringUtil.isEmpty(playerMapper.nameIsExist(name));
	}

	public void savePlayer(Player player) {
		playerMapper.updatePlayer(player);
	}
}
