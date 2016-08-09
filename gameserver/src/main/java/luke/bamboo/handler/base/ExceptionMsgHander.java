package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.ExceptionHandler;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;

@ExceptionHandler()
public class ExceptionMsgHander implements MessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionMsgHander.class);
	
	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		logger.trace("Run exception handler");
	}
}
