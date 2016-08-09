package luke.bamboo.data.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import luke.bamboo.common.util.ExcelUtil;
import luke.bamboo.common.util.JsonUtil;
import luke.bamboo.common.util.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 静态资源加载和使用
 * 
 * @author luke
 */
@Service
public class ResourceDPService {
	private static final Logger logger = LoggerFactory
			.getLogger(ResourceDPService.class);

	private static final String RES_PREX = "WG-";

	@Autowired
	@Qualifier("myJedisPool")
	private JedisPool jedisPool;

	public Map<?, ?> getResource(long baseId) {
		try (Jedis jedis = jedisPool.getResource()) {
			return JsonUtil.toObject(jedis.get(RES_PREX + baseId), Map.class);
		}
	}

	public void loadExcel(String excelpath, String sheet, int startRowNum, int endRowNum, int endCellNum) throws InvalidFormatException, IOException {
		// 加载Excel解析数据组成对象
		logger.info("Load excel :" + excelpath + " sheet:" + sheet);
		ArrayList<ArrayList<String>> rows = ExcelUtil.readExcel(excelpath, sheet, startRowNum, endRowNum, endCellNum);
		if (!rows.isEmpty()) {
			List<Map<String, Object>> list = ExcelUtil.readExcelToMap(excelpath, sheet, endRowNum, endCellNum);
			try (Jedis jedis = jedisPool.getResource()) {
				for(Map map : list) {
					String str = JsonUtil.toJsonString(map);
					if(StringUtil.isNotEmpty(str)) {
						logger.trace(str);
						jedis.set(RES_PREX + map.get("baseid"), str);
					}
				}
			}
			logger.info("加载"+sheet+"资源成功");
		} else {
			logger.error("load excel error: row is empty");
		}
	}

	private Float toFloat(String str) {
		str = str.trim();
		if (StringUtil.isNotEmpty(str)) {
			return Float.valueOf(str);
		}
		return 0f;
	}

	private Integer toInteger(String str) {
		str = str.trim();
		if (StringUtil.isNotEmpty(str)) {
			return Integer.valueOf(str.trim());
		}
		return 0;
	}

	private List<Integer> parseToInts(String arrays) {
		arrays = arrays.trim();
		if (StringUtil.isNotEmpty(arrays)) {
			List<Integer> list = new ArrayList<Integer>();
			for (String str : arrays.split(",|，")) {
				list.add(toInteger(str));
			}
			return list;
		}
		return null;
	}
}