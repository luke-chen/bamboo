package luke.bamboo.gate.service;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import luke.bamboo.data.service.ResourceDPService;

@Service
public class ResourceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
	
	@Value("${excel.building.path}")
	String excelBuildPath;
	
	@Value("${excel.building.sheet}")
	String excelBuildSheetName;
	
	@Value("${excel.soldier.path}")
	String excelSoldierPath;
	
	@Value("${excel.soldier.sheet}")
	String excelSoldierSheetName;
	
	@Value("${excel.hero.path}")
	String excelHeroPath;
	
	@Value("${excel.hero.sheet}")
	String excelHeroSheetName;
	
	@Value("${excel.cityhome.path}")
	String excelCityHomePath;
	
	@Value("${excel.cityhome.sheet}")
	String excelCityHomeSheetName;
	
	@Value("${excel.leadskill.path}")
	String excelLeadSkillPath;
	
	@Value("${excel.leadskill.sheet}")
	String excelLeadSkillSheetName;
	
	@Value("${excel.skill.path}")
	String excelSkillPath;
	
	@Value("${excel.skill.sheet}")
	String excelSkillSheetName;
	
	@Value("${excel.troops.path}")
	String excelTroopsPath;
	
	@Value("${excel.troops.sheet}")
	String excelTroopsSheetName;
	
	@Autowired
	PropertyPlaceholderConfigurer propertyConfigurer;
	
	@Autowired
	ResourceDPService resDPService;
	
	/**
	 * 游戏资源重载
	 */
	public void loadGameResources() {
		try {
			logger.info("加载建筑Excel path:"+excelBuildPath+" sheetName:"+excelBuildSheetName);
			resDPService.loadExcel(excelBuildPath, excelBuildSheetName,  3, 271, 53);
			logger.info("加载士兵Excel path="+excelBuildPath+" sheetName:"+excelSoldierPath);
			resDPService.loadExcel(excelSoldierPath, excelSoldierSheetName, 3, 78, 100);
			logger.info("加载主角Excel path="+excelHeroPath+" sheetName:"+excelHeroPath);
			resDPService.loadExcel(excelHeroPath, excelHeroSheetName,  0, 70,  29);
			logger.info("加载建筑升级Excel path="+excelCityHomePath+" sheetName:"+excelCityHomeSheetName);
			resDPService.loadExcel(excelCityHomePath, excelCityHomeSheetName, 0, 13,  32);
			logger.info("加载技能Excel path="+excelSkillPath+" sheetName:"+excelSkillSheetName);
			resDPService.loadExcel(excelSkillPath, excelSkillSheetName, 0, 120,  42);
			logger.info("加载魔法药水Excel path="+excelLeadSkillPath+" sheetName:"+excelLeadSkillSheetName);
			resDPService.loadExcel(excelLeadSkillPath, excelLeadSkillSheetName,  0, 120,  42);
			logger.info("加载兵营Excel path="+excelTroopsPath+" sheetName:"+excelTroopsSheetName);
			resDPService.loadExcel(excelTroopsPath, excelTroopsSheetName,  0, 120,  42);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
