package luke.bamboo.handler.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import luke.bamboo.annotation.Handler;
import luke.bamboo.handler.MessageHandler;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqEchoMsg;
import luke.bamboo.message.resp.RspEchoMsg;

@Handler( {ReqEchoMsg.class} )
public class EchoMsgHandler implements MessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(EchoMsgHandler.class);

	@Override
	public void process(RequestMessage req, ResponseMessage resp, ChannelHandlerContext ctx) throws Exception {
		logger.trace("Run Echo handler");
		ReqEchoMsg echoMsg = (ReqEchoMsg)req.getData();
		logger.trace(""+echoMsg.getData().equals("test echo"));
		// token 不存在
		RspEchoMsg rsp = new RspEchoMsg();
		rsp.setData(echoMsg.getData());
		resp.setId(MessageID.RSP_ECHO);
		resp.setData(rsp);
	}
}
