package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;

public abstract class ValidTokenMessageHandler extends TokenAndPlayerInChannelHandlerContext
		implements MessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ValidTokenMessageHandler.class);
	
	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		String token = getToken(ctx);
		if (token != null) {
			process(token, req, resp, ctx);
		} else if(resp != null){
			logger.error("invalid token");
			resp.setId(MessageID.RSP_TOKEN_INVALID);
		}
		else {
			logger.error("response is null, maybe it's inactive");
		}
	}

	public abstract void process(String token, RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx)
			throws Exception;
}
