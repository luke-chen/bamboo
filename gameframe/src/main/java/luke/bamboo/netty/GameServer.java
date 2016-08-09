package luke.bamboo.netty;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

@Component
public class GameServer {
	private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

	@Value("${game.ip}")
	private String host;

	@Value("${game.port}")
	private int port;

	public void start() throws Exception {
		logger.info("*** startup netty ***");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		// test work thread group
		// EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// raises a ReadTimeoutException when no data was read
					// within a certain period of time.
					ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(60));
					// raises a WriteTimeoutException when no data was written
					// within a certain period of time.
					ch.pipeline().addLast("writeTimeoutHandler", new WriteTimeoutHandler(60));
//					ch.pipeline().addLast("frameDecoder",
//							new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					ch.pipeline().addLast("frameDecoder",
							new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
					ch.pipeline().addLast("msgDecoder", new MessageDecoder());
					ch.pipeline().addLast("msgEncoder", new MessageEncoder());
					ch.pipeline().addLast(new GameServerHandler());
				}
			});
			b.option(ChannelOption.SO_BACKLOG, 128);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
			// timeout after in.read() was blocked for 10 second
			// b.option(ChannelOption.SO_TIMEOUT, 10*1000);

			b.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 32K
			b.childOption(ChannelOption.SO_RCVBUF, 32 * 1024);
			b.childOption(ChannelOption.SO_SNDBUF, 32 * 1024);

			// set a type of alloc buffer for default
			// b.option(ChannelOption.ALLOCATOR,
			// PooledByteBufAllocator.DEFAULT);
			// b.childOption(ChannelOption.ALLOCATOR,
			// PooledByteBufAllocator.DEFAULT);

			// Bind and start to accept incoming connections.
			logger.info("bind host:"+host+":"+port);
			ChannelFuture f = b.bind(InetAddress.getByName(host), port).sync();

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
