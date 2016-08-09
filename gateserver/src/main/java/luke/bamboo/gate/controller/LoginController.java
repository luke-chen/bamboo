package luke.bamboo.gate.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import luke.bamboo.common.util.StringUtil;
import luke.bamboo.data.domain.Account;
import luke.bamboo.data.domain.GameServerInfo;
import luke.bamboo.data.service.AccountDataService;
import luke.bamboo.data.service.ServerInfoDataService;
import luke.bamboo.gate.service.LoginService;
import luke.bamboo.gate.vo.response.CreateRoleSuccess;
import luke.bamboo.gate.vo.response.Failed;
import luke.bamboo.gate.vo.response.LoginSucessInfo;
import luke.bamboo.gate.vo.response.Result;
import luke.bamboo.gate.vo.response.Success;
import redis.clients.jedis.JedisPool;

@Controller
public class LoginController {
	// LogBack 打印对象
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	@Qualifier("myJedisPool")
	private JedisPool jedisPool;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ServerInfoDataService gateService;
	
    @Autowired
    private AccountDataService accountData;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Result root() {
		return index();
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Result index() {
		return new Success("test");
	}


	/**
	 * 创建登录账户
	 * 
	 * @param accountName
	 * @param password
	 * @param randName
	 * @param debug
	 * @return
	 */
	@RequestMapping(value = "/loginAndRegister", method = RequestMethod.POST)
	@ResponseBody
	public Result loginAndRegister(@RequestParam(value = "accountName", required = false) String accountName,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "randomName", required = false, defaultValue = "0") int randName,
			@RequestParam(value = "debug", required = false, defaultValue = "0") int debug) {
		try {
			if(randName == 1) {
				accountName = UUID.randomUUID().toString();
			}
			logger.info("account login, username:" + accountName + " password:" + password);
			// 登陆创建账户
			Account account = loginService.loginAndRegister(accountName, password);
			// 选择游戏服务器
			GameServerInfo gs = null;
			if(debug == 1) {
				gs = (GameServerInfo) gateService.getDebufServer().values().toArray()[0];
			}
			else {
				gs = (GameServerInfo)gateService.getOfficialServer().values().toArray()[0];
			}
			return new Success(new LoginSucessInfo(account.getUsername(), account.getToken(), gs));
		} catch (Exception e) {
			logger.error("login:", e);
		}
		return new Failed("用户名或密码不正确");
	}

	/**
	 * 创建玩家角色
	 * 
	 * @param name 姓名
	 * @param phyle 种族
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/createRole", method = RequestMethod.POST)
	@ResponseBody
	public Result createRole(@RequestParam(value = "name") String name, @RequestParam(value = "phyle") int phyle,
			@RequestParam(value = "token") String token, @RequestParam(value = "accountName")String accountName) {
		if (!StringUtil.isEmpty(token)) {
			String accountStr = accountData.getLoginAccountId(token);
			if (null != accountStr) {
//				// 查询是否重名
//				if (playerDP.checkPlayerName(name)) {
//					return new Failed("名称已存在");
//				}
//
//				// 创建新角色
//				Long accountId = Long.parseLong(accountStr);
//				// 初始化人物
//				Player player = initPlayer(accountId, name, phyle, token);
//				// 初始化建筑
//				List<Building> buildings = initBuilding(player.getId());
//				logger.info(
//						"账户ID：" + accountId + "_角色ID:" + player.getId() + "_新建角色成功" + "_角色名称：" + name + "_种族:" + phyle);
				return new Success(new CreateRoleSuccess());
			}
		}
		return new Failed("invalid token");
	}
}