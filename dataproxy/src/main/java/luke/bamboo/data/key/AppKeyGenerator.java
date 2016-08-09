package luke.bamboo.data.key;

/**
 * @author luke
 */
public class AppKeyGenerator {
	
	public static final String TOKEN_FORMATER = "%s-login";
	
	public static String getTokenKey(String token) {
		return String.format(TOKEN_FORMATER, token);
	}
}
