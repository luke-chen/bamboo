package luke.bamboo.handler.base;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.Handler;
import luke.bamboo.data.service.AccountDataService;
import luke.bamboo.data.service.PlayerDataService;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.logic.service.player.PlayerLogicService;
import luke.bamboo.message.Message;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqLoginMsg;
import luke.bamboo.netty.GameContext;

@Handler( {ReqLoginMsg.class} )
public class LoginMsgHandler extends TokenAndPlayerInChannelHandlerContext implements MessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginMsgHandler.class);
	
	@Autowired
	private AccountDataService accountData;
	
	@Autowired
	private PlayerDataService playerData;

	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		
		logger.trace("Run login handler");
		String token = ((ReqLoginMsg)req.getData()).getToken();

		// token 不存在
		Map<String, String> accountInfo;
		if ((accountInfo = accountData.loginGameServer(token)) != null) {
			try {
				String oldToken = getToken(ctx); 
				if (oldToken == null) {
					// 登陆成功
					long playerId = playerData.getPlayerByAccount(Integer.valueOf(accountInfo.get("accountId"))).getId();
					setToken(ctx, token);
					setPlayerId(ctx, playerId);
					GameContext.serviceLogin(playerId);
					GameContext.addChannel(playerId, ctx.channel());
					logger.info("user login token:" + token + " playid:" + playerId);
				} else {
					// tokenKey already has a value, not first login
					//GameContext.serviceOnine(playerId);
				}
				resp.setId(MessageID.RSP_LOGIN_OK);
			} catch (Exception ex) {
				resp.setId(MessageID.RSP_LOGIN_ERROR);
				logger.error("", ex);
			}
		} else {
			// token 验证失败
			resp.setId(MessageID.RSP_TOKEN_INVALID);
		}
	}
}
