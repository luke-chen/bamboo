package luke.bamboo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;

/**
 * A Runable for run Messagehandler
 * 
 * @author luke
 */
public class MessageHandlerRunable implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandlerRunable.class);
	private MessageHandler handler;
	private RequestMessage req;
	private ResponseMessage resp;
	private ChannelHandlerContext ctx;

	public MessageHandlerRunable(MessageHandler handler, RequestMessage req, ResponseMessage resp,
			ChannelHandlerContext ctx) {
		this.handler = handler;
		this.req = req;
		this.resp = resp;
		this.ctx = ctx;
	}

	@Override
	public void run() {
		try {
			long st = System.currentTimeMillis();
			handler.process(req, resp, ctx);
			if (resp.getId() != 0) {
				ctx.writeAndFlush(resp);
			}
			logger.trace(
					String.format("[finish handler] [%dms] [req:%s %s] [resp:%s %s]", System.currentTimeMillis() - st,
							req.getId(), "", resp.getId(), ""));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ReferenceCountUtil.release(req);
		}
	}
}
