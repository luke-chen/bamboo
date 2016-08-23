package luke.bamboo.client.netty;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import luke.bamboo.client.TimerCounter;
import luke.bamboo.message.id.MessageID;
import luke.bamboo.message.req.ReqEchoMsg;
import luke.bamboo.message.resp.RspEchoMsg;
import luke.bamboo.netty.JsonUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	int curLoop;
	TimerCounter tc = new TimerCounter();

	public void writeAndFlush(Channel ch) throws UnsupportedEncodingException {
		ByteBuf buffer = PooledByteBufAllocator.DEFAULT.heapBuffer(10);
		buffer.writeInt(0);
		buffer.writeShort(MessageID.REQ_ECHO);// 包长占2字节
		ReqEchoMsg req = new ReqEchoMsg();
		req.setData("this is my test");
		buffer.writeBytes(JsonUtil.toByteArray(req));
		buffer.setInt(0, buffer.writerIndex() - 4);
		ch.writeAndFlush(buffer);
		tc.addStartTime();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		EchoClient.list.add(tc);
		writeAndFlush(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		tc.getSpendTime();
		ByteBuf in = (ByteBuf) msg;
		int len = in.readInt();
		short msgId = in.readShort();
		long t = in.readLong();
		byte[] data = new byte[len - 2 - 8];
		in.readBytes(data);
		RspEchoMsg rsp = JsonUtil.toObject(data, RspEchoMsg.class);
		System.out.println(rsp.getData());
		if (++curLoop < EchoClient.loopNum) {
			try {
				Thread.sleep(EchoClient.intval);
				writeAndFlush(ctx.channel());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			tc.printReport();
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}