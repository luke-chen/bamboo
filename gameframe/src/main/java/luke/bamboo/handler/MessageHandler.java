package luke.bamboo.handler;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;

/**
 * Top Message Handler
 * @author Luke
 */
public interface MessageHandler {

	void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception;
}
