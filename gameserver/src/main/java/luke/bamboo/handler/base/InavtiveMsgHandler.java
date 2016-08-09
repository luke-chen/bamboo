package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.InactiveHandler;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.netty.GameContext;

@InactiveHandler()
public class InavtiveMsgHandler extends TokenAndPlayerInChannelHandlerContext implements MessageHandler  {
	
	private static final Logger logger = LoggerFactory.getLogger(InavtiveMsgHandler.class);
	
	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		logger.trace("Run inactive handler");
		String token = getToken(ctx);
		
		if (token != null) {
			// 合法用户掉线
			long playerId = getPlayerId(ctx);
			GameContext.serviceLogout(playerId);
			GameContext.removeChannel(playerId);
		}
		else {
			// 非法用户掉线
		}
	}
}