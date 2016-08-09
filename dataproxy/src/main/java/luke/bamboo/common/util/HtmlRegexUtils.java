package luke.bamboo.common.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlRegexUtils {
	private static final Pattern IMG_PATTERN = Pattern.compile("<img [^>]*");
	
	private static final Pattern SRC_PATTERN = Pattern.compile("src=\"[^\"]*");
	
	public static ArrayList<String> getImgURL(String html, String host) {
		ArrayList<String> imgs = new ArrayList<String>();
		Matcher imgMatcher = IMG_PATTERN.matcher(html);
		while (imgMatcher.find()) {
			String imgTag = imgMatcher.group();
			Matcher srcMatcher = SRC_PATTERN.matcher(imgTag);
			if (srcMatcher.find()) {
				String src = srcMatcher.group();
				String imgUrl = src.substring(src.indexOf("\"") + 1, src.length()).trim();
				if(imgUrl.indexOf("/") == 0)
					imgUrl = host+imgUrl;
				imgs.add(imgUrl);
			}
		}
		return imgs;
	}
	
	public static ArrayList<String> getImgTags(String html) {
		ArrayList<String> imgs = new ArrayList<String>();
		Matcher imgMatcher = IMG_PATTERN.matcher(html);
		while (imgMatcher.find()) {
			String imgTag = imgMatcher.group();
			imgs.add(imgTag);
		}
		return imgs;
	}
	
	public static String delHtml(String htmlStr) {
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_script = "<[/s]*?script[^>]*?>[/s/S]*?<[/s]*?//[/s]*?script[/s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[/s/S]*?<//script>
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr;// 返回文本字符串
	}
	
	public static String getPureWords(String text) {
		return text.replaceAll("[^a-z0-9A-Z\u4e00-\u9fa5]*", "");
	}
	
	public static void main(String[] args) {
		String desc = "<p>颜值高可以不会做饭！xxhh\n</p><p><img src=\"http://qunimei.oupeng.com/post_imgs/57047becf1a81d04d1595f42/1.jpg\" width=\"440\" height=\"754\"></p>321312312发达<img src=\"/post_imgs/57047becf1a81d04d1595f42/1.jpg\" width=\"440\" height=\"754\">的是。。。";
		ArrayList<String> list = HtmlRegexUtils.getImgURL(desc, "http://qunimei.oupeng.com");
		for(String str : list) {
			System.out.println(str);
		}
		System.out.println(delHtml(desc));
		
		System.out.println("prue words:"+HtmlRegexUtils.getPureWords("~`as发达~!2好@ -_=#$%^*()_+}{|\":;'.,/ ?'"));
	}
}
