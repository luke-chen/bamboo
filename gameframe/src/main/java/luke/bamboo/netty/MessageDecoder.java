package luke.bamboo.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import luke.bamboo.handler.MessageHandlerEngine;
import luke.bamboo.message.RequestMessage;

public class MessageDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
			
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if (in.isReadable()) {
			short msgId = in.readShort();
			RequestMessage msg = new RequestMessage(msgId);
			if (in.readableBytes() > 0) {
				msg.setData(JsonUtil.toObject(in.readBytes(in.readableBytes()).array(),
						MessageHandlerEngine.getInstance().getClazz(msgId)));
			}
			out.add(msg);
			logger.trace(JsonUtil.toJsonString(msg));
		}
	}
}
