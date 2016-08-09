package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.UnknownHandler;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;

@UnknownHandler()
public class UnknownMsgHander implements MessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(UnknownMsgHander.class);

	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		logger.trace("Run unknow handler");
		resp.setId(MessageID.RSP_UNKNOWN);
	}
}
