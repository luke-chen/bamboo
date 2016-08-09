package luke.bamboo.handler.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class TokenAndPlayerInChannelHandlerContext {
	// token
	private static final AttributeKey<String> TOKEN_KEY = AttributeKey.newInstance("token");
	private static final AttributeKey<Long> PLAYER_ID_KRY = AttributeKey.newInstance("playerId");
	
	public String getToken(ChannelHandlerContext ctx) {
		return ctx.attr(TOKEN_KEY).get();
	}
	
	public void setToken(ChannelHandlerContext ctx, String token) {
		ctx.attr(TOKEN_KEY).set(token);
	}
	
	public void setPlayerId(ChannelHandlerContext ctx, long playerId) {
		ctx.attr(PLAYER_ID_KRY).set(playerId);
	}
	
	public long getPlayerId(ChannelHandlerContext ctx) {
		return ctx.attr(PLAYER_ID_KRY).get();
	}
}
