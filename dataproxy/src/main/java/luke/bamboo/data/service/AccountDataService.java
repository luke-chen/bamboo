package luke.bamboo.data.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import luke.bamboo.common.security.MD5;
import luke.bamboo.common.util.StringUtil;
import luke.bamboo.data.db.mapper.AccountMapper;
import luke.bamboo.data.domain.Account;
import luke.bamboo.data.key.AppKeyGenerator;

@Service
public class AccountDataService {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	@Qualifier("myJedisPool")
	private JedisPool jedisPool;

	@Transactional
	public void newAccount(String username, String password) {
		accountMapper.addUser(username, password);
	}

	/**
	 * 用户登陆
	 * 
	 * @param username
	 * @param password
	 * @return a token for connecting game server will be returned
	 */
	public Account loginAccount(String username, String password) {
		if (!StringUtil.isEmpty(username, password)) {
			Account account = accountMapper.getUser(username, password);
			// 用户已存在，返回token
			if (account != null) {
				String token = MD5.MD5Hex(account.getUsername() + account.getPassword() + System.currentTimeMillis());
				account.setToken(token);
				// 用户信息放入缓存
				try (Jedis jedis = jedisPool.getResource()) {
					Map<String, String> accountInfo = new HashMap<String, String>();
					accountInfo.put("username", account.getUsername());
					accountInfo.put("accountId", String.valueOf(account.getId()));
					String loginToken = AppKeyGenerator.getTokenKey(token);
					// 没有重置 exprie
					jedis.hmset(loginToken, accountInfo);
					// 一分钟还没有登陆游戏服务器 ，token将从cache中删除
					jedis.expire(loginToken, 60);
				}
				return account;
			}
		}
		return null;
	}

	/**
	 * 登陆游戏服务器
	 * 
	 * @param token
	 * @return
	 */
	public Map<String, String> loginGameServer(String token) {
		final String loginToken = AppKeyGenerator.getTokenKey(token);;
		try (Jedis jedis = jedisPool.getResource()) {
			if (jedis.exists(loginToken)) {
				Map<String, String> accountInfo = jedis.hgetAll(loginToken);
				jedis.del(loginToken);
				return accountInfo;
			}
		}
		return null;
	}
	
	public String getLoginAccountId(String token) {
		try(Jedis jedis = jedisPool.getResource()) {
			return jedis.hgetAll(AppKeyGenerator.getTokenKey(token)).get("accountId");
		}
	}
}
