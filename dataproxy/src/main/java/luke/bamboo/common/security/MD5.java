package luke.bamboo.common.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static byte[] MD5Binary(String text, String charset) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
			msgDigest.update(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding:" + charset, e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		byte[] md5 = msgDigest.digest();
		return md5;
	}

	public static String MD5Hex(String text, String charset) {
		byte[] md5bin = MD5Binary(text, charset);
		char[] md5hex = MyNumber.BinaryToHex(md5bin);
		return new String(md5hex);
	}

	public static String MD5Hex(String text) {
		return MD5Hex(text, "utf-8");
	}
}
