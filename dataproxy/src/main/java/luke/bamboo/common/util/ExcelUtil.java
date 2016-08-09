package luke.bamboo.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Description: 解析Excel公用类
 * @author luke
 */
public class ExcelUtil {
	private static final double ZEOR = 0.00000000000000000;
	private static final Logger logger = LoggerFactory
			.getLogger(ExcelUtil.class);

	/**
	 * @param filepath
	 *            文件路径
	 * @param startRowNum
	 *            从第几行开始读
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static ArrayList<ArrayList<String>> readExcel(String filepath,
			String sheetName, int startRowNum, int endRowNum, int endCellNum)
			throws IOException, InvalidFormatException {
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		Workbook workBook = null;
		try {
			File excelFile = new File(filepath);
			if (excelFile.exists() && excelFile.isFile() && excelFile.canRead()) {
				workBook = new XSSFWorkbook(excelFile);
				// for (int numSheet = 0; numSheet <
				// workBook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workBook.getSheet(sheetName);
				if (sheet != null) {
					// 循环行Row
					int maxRowNum = endRowNum < 0 ? sheet.getLastRowNum()
							: endRowNum;
					for (int i = startRowNum; i <= maxRowNum; i++) {
						// logger.debug("row "+i);
						Row row = sheet.getRow(i);
						if (row == null) {
							continue;
						}
						// 循环列Cell
						int maxCellNum = endCellNum < 0 ? row.getLastCellNum()
								: endCellNum;
						ArrayList<String> cells = new ArrayList<String>();
						for (int cellNum = 0; cellNum <= maxCellNum; cellNum++) {
							Cell cell = row.getCell(cellNum);
							if (cell == null
									|| StringUtil.isEmpty(getValue(cell))) {
								// logger.info("value of cell is empty");
								cells.add("");
							} else if (StringUtil.isNotEmpty(getValue(cell))) {
								// logger.info(getValue(cell));
								cells.add(getValue(cell));
							}
						}
						rows.add(cells);
					}
				}
			}
		} finally {
			if (workBook != null)
				workBook.close();
		}
		return rows;
	}

	public static List<Map<String, Object>> readExcelToMap(String filepath,
			String sheetName, int endRowNum, int endCellNum)
			throws IOException, InvalidFormatException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		XSSFWorkbook workBook = null;
		try {
			File excelFile = new File(filepath);
			if (excelFile.exists() && excelFile.isFile() && excelFile.canRead()) {
				workBook = new XSSFWorkbook(excelFile);
				XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator( workBook);
				// for (int numSheet = 0; numSheet <
				// workBook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workBook.getSheet(sheetName);
				if (sheet != null) {
					// 循环行Row
					int maxRowNum = endRowNum < 0 ? sheet.getLastRowNum()
							: endRowNum;
					// 字段名称
					ArrayList<String> fieldNames = new ArrayList<String>();
					// 字段类型
					ArrayList<String> fieldType = new ArrayList<String>();
					for (int i = 1; i <= maxRowNum; i++) {
						Row row = sheet.getRow(i);
						if (row != null) {
							int maxCellNum = endCellNum < 0 ? row.getLastCellNum() : endCellNum;
							// 循环列Cell
							Map<String, Object> cells = new HashMap<String, Object>();
							for (int cellNum = 0; cellNum < maxCellNum; cellNum++) {
								Cell cell = row.getCell(cellNum);
								if (i == 1) {
									// 字段名称初始化
									if (cell == null || StringUtil.isEmpty(getValue(cell))) {
										// logger.info("value of cell is empty");
									} else if (StringUtil.isNotEmpty(getValue(cell))) {
										// logger.info(getValue(cell));
										fieldNames.add(cell.getStringCellValue().trim());
									}
								} else if (i == 2) {
									// 字段类型初始化
									if (cell == null || StringUtil.isEmpty(getValue(cell))) {
										// logger.info("value of cell is empty");
									} else if (StringUtil.isNotEmpty(getValue(cell))) {
										// logger.info(getValue(cell));
										fieldType.add(cell.getStringCellValue().trim());
									}
								} else if (i > 2) {
									if (cell != null) {
										String type = fieldType.get(cellNum).trim();
										String field = fieldNames.get(cellNum).trim();
										Object value = null;
//										if(i == 218 && cellNum == 50 || i == 214 && cellNum == 50 ) {
//											System.out.println("catch");
//										}

										if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
											value = cell.getBooleanCellValue();
										} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
											value = cell.getNumericCellValue();
										} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
											value = cell.getStringCellValue();
										} else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
											value = evaluator.evaluate(cell).getNumberValue();
										}
										
										logger.info((i+1)+" "+cellNum+field+":"+value);
										System.out.println((i+1)+" "+cellNum+field+":"+value);
										
										if (type.equals("int")) {
											cells.put(field, value == null ? 0 : Float.valueOf(value.toString()).intValue());
										} else if (type.equals("float")) {
											cells.put(field, value == null ? 0f : Float.valueOf(value.toString()));
										} else if (type.equals("string")) {
											cells.put(field, value == null ? "" : (String)value);
										} else if (type.equals("array") || type.equals("intArray")) {
											cells.put(field, value == null ? null : parseToInts((String)value));
										} else if (type.equals("floatArray")) {
											cells.put(field, value == null ? null : parseToFloats((String)value));
										} else if (type.equals("stringArray")) {
											cells.put(field, value == null ? null : ((String)value).split(",|，"));
										}
									}
									else {
										logger.info("null cell");	
									}
								}
							}
							rows.add(cells);
						}
					}
				}
			}
		} finally {
			if (workBook != null)
				workBook.close();
		}
		return rows;
	}

	private static String getValue(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			double value = cell.getNumericCellValue();
			if (value % 1 == ZEOR)
				return String.valueOf((int) value);
			else
				return String.valueOf(value);
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return String.valueOf(cell.getStringCellValue());
		} else {
			return null;
		}
	}

	private static Float toFloat(String str) {
		str = str.trim();
		if (StringUtil.isNotEmpty(str)) {
			return Float.valueOf(str);
		}
		return 0f;
	}

	private static Integer toInteger(String str) {
		str = str.trim();
		if (StringUtil.isNotEmpty(str)) {
			return Long.valueOf(str.trim()).intValue();
		}
		return 0;
	}

	private static List<Integer> parseToInts(String arrays) {
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

	private static List<Float> parseToFloats(String arrays) {
		arrays = arrays.trim();
		if (StringUtil.isNotEmpty(arrays)) {
			List<Float> list = new ArrayList<Float>();
			for (String str : arrays.split(",|，")) {
				list.add(toFloat(str));
			}
			return list;
		}
		return null;
	}

	private List<String> parseToStrs(String arrays) {
		arrays = arrays.trim();
		if (StringUtil.isNotEmpty(arrays)) {
			List<String> list = new ArrayList<String>();
			for (String str : list) {
				list.add(str);
			}
			return list;
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			System.out.println(JsonUtil.toJsonString(ExcelUtil.readExcelToMap(
					"E:/wego_workspace/wego/wego-dataproxy/hero.xlsx",
					"Sheet1", 70, 29)));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}