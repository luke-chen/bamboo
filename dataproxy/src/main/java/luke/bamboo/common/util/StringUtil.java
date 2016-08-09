package luke.bamboo.common.util;

public class StringUtil {

	/**
	 * @param str
	 * @return true if string is null or all consist of whitespace
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * @param strs
	 * @return true if any string is null or all consist of whitespace
	 */
	public static boolean isEmpty(String... strs) {
		if (strs != null && strs.length > 0)
			for (String str : strs)
				if (!(str == null || str.trim().isEmpty()))
					return false;
		return true;
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.isEmpty(""));
		System.out.println(StringUtil.isEmpty(" a"));
		System.out.println(StringUtil.isEmpty("   "));
		System.out.println("<==========================>");
		System.out.println(StringUtil.isEmpty("", "a "));
		System.out.println(StringUtil.isEmpty("  ", " ", ""));
		System.out.println(StringUtil.isEmpty("a  ", " ", ""));
		System.out.println(StringUtil.isEmpty("abc", "ddd", "d  g"));
	}
}
