package luke.bamboo.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {
	/**
	 * 获取UTC时间(国际标准时间)
	 * @param date java.util.Date，为空则默认为当前
	 * @return String solr的UTC时间(格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
	 * */
	public static String getUTCTime(Date date){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		if(date == null){
			return sdf.format(new Date());
		}
		return sdf.format(date);
	}
}
