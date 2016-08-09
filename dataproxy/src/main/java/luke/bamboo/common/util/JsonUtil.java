package luke.bamboo.common.util;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * An utility of Json
 * 
 * @author luke
 */
public class JsonUtil {

	public static String toJsonString(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	public static <T> T toObject(byte[] byteArray, Class<T> clazz) {
		return JSON.parseObject(byteArray, clazz);
	}
	
	public static Object toObject(String json) {
		return JSON.parseObject(json);
	}

	public static byte[] toByteArray(Object obj) throws UnsupportedEncodingException {
		return JSON.toJSONBytes(obj, SerializerFeature.WriteNonStringKeyAsString);
	}
}
