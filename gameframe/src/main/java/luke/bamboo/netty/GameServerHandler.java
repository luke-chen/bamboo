package luke.bamboo.netty;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import luke.bamboo.handler.MessageHandlerEngine;
import luke.bamboo.handler.MessageHandlerRunable;
import luke.bamboo.message.Message;
import luke.bamboo.message.RequestMessage;
import luke.bamboo.message.ResponseMessage;

/**
 * Handles a server-side channel.
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);
	private static AtomicLong activeNum = new AtomicLong();
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		logger.trace("active "+activeNum.getAndIncrement());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.trace("inactive "+activeNum.getAndDecrement());
		try {
			MessageHandlerEngine.getInstance().getInactiveHandler().process(null, null, ctx);
		} catch (Exception e) {
			logger.error("channelInactive", e);
		}
		ctx.close();
	}

	/**
	 * It's handle for no thread pool
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			long st = System.currentTimeMillis();
			RequestMessage req = (RequestMessage) msg;
			short id = req.getId();
			ResponseMessage resp = new ResponseMessage();
			MessageHandlerEngine.getInstance().getMessageHandler(id).process(req, resp, ctx);
			if (resp.getId() != 0) {
				ctx.writeAndFlush(resp);
			}
			logger.trace(
					String.format("[finish handler]-[%dms]-[req:%s %s]-[resp:%s %s]", System.currentTimeMillis() - st,
							id, req.getData(), resp.getId(), resp.getData()));
		} catch (Exception ex) {
			logger.error("channelRead", ex);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	/**
	 * 2. It's for thread pool
	 */
//	private ThreadPoolExecutor pool = new ThreadPoolExecutor(8, 16, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
//
//	public void channelRead1(ChannelHandlerContext ctx, Object msg) {
//		logger.trace("read");
//		RequestMessage req = (RequestMessage) msg;
//		pool.execute(new MessageHandlerRunable(MessageHandlerEngine.getInstance().getMessageHandler(req.getId()), req,
//				new ResponseMessage(), ctx));
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("exceptionCaught", cause);
		try {
			MessageHandlerEngine.getInstance().getExceptionHandler().process(null, null, ctx);
		} catch (Exception e) {
			logger.error("exceptionCaught", e);
		} finally {
			// Close the connection when an exception is raised.
			ctx.close();
		}
	}
}