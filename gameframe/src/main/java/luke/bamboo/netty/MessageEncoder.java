package luke.bamboo.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import luke.bamboo.message.ResponseMessage;

public class MessageEncoder extends MessageToByteEncoder<ResponseMessage> {
	private static final Logger logger = LoggerFactory
			.getLogger(MessageEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, ByteBuf out) {
		out.writeShort(msg.getId());
		out.writeLong(msg.getT());
		if (msg.getData() != null) {
			try {
				out.writeBytes(JsonUtil.toByteArray(msg.getData()));
				logger.trace(JsonUtil.toJsonString(msg));
			} catch (Exception e) {
				logger.error("Encode error", e);
			}
		}
	}
}
