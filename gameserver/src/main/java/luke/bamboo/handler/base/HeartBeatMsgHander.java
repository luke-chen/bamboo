package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.Handler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqHeartBeat;

/**
 * Heart beat
 * 
 * @author luke
 */
@Handler({ReqHeartBeat.class})
public class HeartBeatMsgHander extends ValidTokenMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatMsgHander.class);
	
	@Override
	public void process(String token, RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		logger.trace("Run heartbeat handler");
		resp.setId(MessageID.RSP_HEART_BEAT);
	}
}