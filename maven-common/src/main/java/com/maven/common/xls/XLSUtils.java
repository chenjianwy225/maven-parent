package com.maven.common.xls;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.maven.common.StringUtils;

/**
 * XLS生成类
 * 
 * @author chenjian
 * @createDate 2019-09-17
 */
public class XLSUtils {

	/**
	 * 生成文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetName
	 *            工作簿名称
	 * @param titleName
	 *            标题名称
	 * @param titles
	 *            列表头名称集合
	 * @param datas
	 *            数据集合
	 * @param checks
	 *            验证数据是否需要标注集合
	 * @return
	 */
	public static boolean createFile(String filePath, String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
		boolean success = true;
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toLowerCase();

		if (fileType.equalsIgnoreCase("xls")) {
			createXLS(filePath, sheetName, titleName, titles, datas, checks);
		} else if (fileType.equalsIgnoreCase("xlsx")) {
			createXLSX(filePath, sheetName, titleName, titles, datas, checks);
		} else {
			success = false;
		}

		return success;
	}

	/**
	 * 生成XLS文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetName
	 *            工作簿名称
	 * @param titleName
	 *            标题名称
	 * @param titles
	 *            列表头名称集合
	 * @param datas
	 *            数据集合
	 * @param checks
	 *            验证数据是否需要标注集合
	 */
	private static void createXLS(String filePath, String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
		FileOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		try {
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet(sheetName);
			int rowIndex = 0;
			boolean setWidth = false;

			Font tFont = workbook.createFont();
			tFont.setBold(true);
			tFont.setColor(HSSFColorPredefined.BLACK.getIndex());
			tFont.setFontHeightInPoints((short) 12);

			Font dFont = workbook.createFont();
			dFont.setColor(HSSFColorPredefined.RED.getIndex());

			HSSFCellStyle tnStyle = workbook.createCellStyle();
			tnStyle.setAlignment(HorizontalAlignment.CENTER);
			tnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			tnStyle.setFont(tFont);

			HSSFCellStyle tStyle = workbook.createCellStyle();
			tStyle.setAlignment(HorizontalAlignment.CENTER);
			tStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			tStyle.setFont(tFont);
			tStyle.setBorderLeft(BorderStyle.THIN);
			tStyle.setBorderTop(BorderStyle.THIN);
			tStyle.setBorderRight(BorderStyle.THIN);
			tStyle.setBorderBottom(BorderStyle.THIN);

			HSSFCellStyle dStyle = workbook.createCellStyle();
			dStyle.setAlignment(HorizontalAlignment.CENTER);
			dStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			dStyle.setBorderLeft(BorderStyle.THIN);
			dStyle.setBorderTop(BorderStyle.THIN);
			dStyle.setBorderRight(BorderStyle.THIN);
			dStyle.setBorderBottom(BorderStyle.THIN);

			HSSFCellStyle dStyleOne = workbook.createCellStyle();
			dStyleOne.setAlignment(HorizontalAlignment.CENTER);
			dStyleOne.setVerticalAlignment(VerticalAlignment.CENTER);
			dStyleOne.setFont(dFont);
			dStyleOne.setBorderLeft(BorderStyle.THIN);
			dStyleOne.setBorderTop(BorderStyle.THIN);
			dStyleOne.setBorderRight(BorderStyle.THIN);
			dStyleOne.setBorderBottom(BorderStyle.THIN);

			// 判断是否有标题
			if (StringUtils.isNotEmpty(titleName)) {
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 600);
				cell = row.createCell(0);
				cell.setCellValue(titleName);
				cell.setCellStyle(tnStyle);

				CellRangeAddress region = new CellRangeAddress(0, 0, 0,
						titles.size() - 1);
				sheet.addMergedRegion(region);

				RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);

				rowIndex += 1;
			}

			// 判断是否有标头
			if (StringUtils.isNotEmpty(titles) && titles.size() > 0) {
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 600);

				for (int i = 0; i < titles.size(); i++) {
					cell = row.createCell(i, CellType.STRING);
					cell.setCellValue(titles.get(i));
					cell.setCellStyle(tStyle);

					sheet.setColumnWidth(i, 15 * 256);
				}

				rowIndex += 1;
				setWidth = true;
			}

			// 判断是否有数据
			if (StringUtils.isNotEmpty(datas) && datas.size() > 0) {
				for (Map<String, Object> map : datas) {
					row = sheet.createRow(rowIndex);
					row.setHeight((short) 300);

					Map<String, Object> newMap = new LinkedHashMap<String, Object>();
					Iterator<Entry<String, Object>> iterator = map.entrySet()
							.iterator();

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();
						String key = entry.getKey();
						Object object = entry.getValue();

						if (object instanceof Object[]) {
							Object[] objs = (Object[]) object;

							for (int i = 0; i < objs.length; i++) {
								newMap.put(key + "-" + i, objs[i]);
							}
						} else {
							newMap.put(key, object);
						}
					}

					if (newMap.size() > 0) {
						iterator = newMap.entrySet().iterator();

						int j = 0;
						while (iterator.hasNext()) {
							Entry<String, Object> entry = iterator.next();
							String key = entry.getKey();
							Object object = entry.getValue();

							CellType cellType = CellType.STRING;
							HSSFCellStyle cellStyle = dStyle;

							if (object instanceof Boolean) {
								cellType = CellType.BOOLEAN;
							} else if (object instanceof Number) {
								cellType = CellType.NUMERIC;

								if (StringUtils.isNotEmpty(checks)
										&& checks.size() > 0) {
									boolean isCheck = false;
									Number value = 0;
									Iterator<Entry<String, Number>> iterator2 = checks
											.entrySet().iterator();

									while (iterator2.hasNext()) {
										Entry<String, Number> entry2 = iterator2
												.next();

										if (key.indexOf(entry2.getKey()) != -1) {
											isCheck = true;
											value = entry2.getValue();
											break;
										}
									}

									if (isCheck) {
										Number number = (Number) object;
										if (number.floatValue() < value
												.floatValue()) {
											cellStyle = dStyleOne;
										}
									}
								}
							}

							cell = row.createCell(j, cellType);
							cell.setCellValue(object.toString());
							cell.setCellStyle(cellStyle);

							j += 1;
						}
					}

					rowIndex += 1;
				}

				if (!setWidth) {
					setWidth = true;
				}
			}

			outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成XLSX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetName
	 *            工作簿名称
	 * @param titleName
	 *            标题名称
	 * @param titles
	 *            列表头名称集合
	 * @param datas
	 *            数据集合
	 * @param checks
	 *            验证数据是否需要标注集合
	 */
	private static void createXLSX(String filePath, String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;

		try {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet(sheetName);
			int rowIndex = 0;
			boolean setWidth = false;

			Font tFont = workbook.createFont();
			tFont.setBold(true);
			tFont.setColor(HSSFColorPredefined.BLACK.getIndex());
			tFont.setFontHeightInPoints((short) 12);

			Font dFont = workbook.createFont();
			dFont.setColor(HSSFColorPredefined.RED.getIndex());

			XSSFCellStyle tnStyle = workbook.createCellStyle();
			tnStyle.setAlignment(HorizontalAlignment.CENTER);
			tnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			tnStyle.setFont(tFont);

			XSSFCellStyle tStyle = workbook.createCellStyle();
			tStyle.setAlignment(HorizontalAlignment.CENTER);
			tStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			tStyle.setFont(tFont);
			tStyle.setBorderLeft(BorderStyle.THIN);
			tStyle.setBorderTop(BorderStyle.THIN);
			tStyle.setBorderRight(BorderStyle.THIN);
			tStyle.setBorderBottom(BorderStyle.THIN);

			XSSFCellStyle dStyle = workbook.createCellStyle();
			dStyle.setAlignment(HorizontalAlignment.CENTER);
			dStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			dStyle.setBorderLeft(BorderStyle.THIN);
			dStyle.setBorderTop(BorderStyle.THIN);
			dStyle.setBorderRight(BorderStyle.THIN);
			dStyle.setBorderBottom(BorderStyle.THIN);

			XSSFCellStyle dStyleOne = workbook.createCellStyle();
			dStyleOne.setAlignment(HorizontalAlignment.CENTER);
			dStyleOne.setVerticalAlignment(VerticalAlignment.CENTER);
			dStyleOne.setFont(dFont);
			dStyleOne.setBorderLeft(BorderStyle.THIN);
			dStyleOne.setBorderTop(BorderStyle.THIN);
			dStyleOne.setBorderRight(BorderStyle.THIN);
			dStyleOne.setBorderBottom(BorderStyle.THIN);

			// 判断是否有标题
			if (StringUtils.isNotEmpty(titleName)) {
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 600);
				cell = row.createCell(0);
				cell.setCellValue(titleName);
				cell.setCellStyle(tnStyle);

				CellRangeAddress region = new CellRangeAddress(0, 0, 0,
						titles.size() - 1);
				sheet.addMergedRegion(region);

				RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
				RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);

				rowIndex += 1;
			}

			// 判断是否有标头
			if (StringUtils.isNotEmpty(titles) && titles.size() > 0) {
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 600);

				for (int i = 0; i < titles.size(); i++) {
					cell = row.createCell(i, CellType.STRING);
					cell.setCellValue(titles.get(i));
					cell.setCellStyle(tStyle);

					sheet.setColumnWidth(i, 15 * 256);
				}

				rowIndex += 1;
				setWidth = true;
			}

			// 判断是否有数据
			if (StringUtils.isNotEmpty(datas) && datas.size() > 0) {
				for (Map<String, Object> map : datas) {
					row = sheet.createRow(rowIndex);
					row.setHeight((short) 300);

					Map<String, Object> newMap = new LinkedHashMap<String, Object>();
					Iterator<Entry<String, Object>> iterator = map.entrySet()
							.iterator();

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();
						String key = entry.getKey();
						Object object = entry.getValue();

						if (object instanceof Object[]) {
							Object[] objs = (Object[]) object;

							for (int i = 0; i < objs.length; i++) {
								newMap.put(key + "-" + i, objs[i]);
							}
						} else {
							newMap.put(key, object);
						}
					}

					if (newMap.size() > 0) {
						iterator = newMap.entrySet().iterator();

						int j = 0;
						while (iterator.hasNext()) {
							Entry<String, Object> entry = iterator.next();
							String key = entry.getKey();
							Object object = entry.getValue();

							CellType cellType = CellType.STRING;
							XSSFCellStyle cellStyle = dStyle;

							if (object instanceof Boolean) {
								cellType = CellType.BOOLEAN;
							} else if (object instanceof Number) {
								cellType = CellType.NUMERIC;

								if (StringUtils.isNotEmpty(checks)
										&& checks.size() > 0) {
									boolean isCheck = false;
									Number value = 0;
									Iterator<Entry<String, Number>> iterator2 = checks
											.entrySet().iterator();

									while (iterator2.hasNext()) {
										Entry<String, Number> entry2 = iterator2
												.next();

										if (key.indexOf(entry2.getKey()) != -1) {
											isCheck = true;
											value = entry2.getValue();
											break;
										}
									}

									if (isCheck) {
										Number number = (Number) object;
										if (number.floatValue() < value
												.floatValue()) {
											cellStyle = dStyleOne;
										}
									}
								}
							}

							cell = row.createCell(j, cellType);
							cell.setCellValue(object.toString());
							cell.setCellStyle(cellStyle);

							j += 1;
						}
					}

					rowIndex += 1;
				}

				if (!setWidth) {
					setWidth = true;
				}
			}

			outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}