package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;


public class LogoutMsgHandler extends ValidTokenMessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);

	@Override
	public void process(String token, RequestMessage req, ResponseMessage resp,
			ChannelHandlerContext ctx) throws Exception {
		logger.trace("RUn logout handler");
	}
}