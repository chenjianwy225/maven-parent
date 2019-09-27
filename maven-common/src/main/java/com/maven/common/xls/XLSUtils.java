package com.maven.common.xls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 读写XLS类
 * 
 * @author chenjian
 * @createDate 2019-09-17
 */
public class XLSUtils {

	private static Logger logger = LoggerFactory.getLogger(XLSUtils.class);

	// XLS文件后缀名
	private static final String XLS_NAME = "xls";

	// XLSX文件后缀名
	private static final String XLSX_NAME = "xlsx";

	// XLS文件后缀索引
	public static final int XLS_INDEX = 1;

	// XLSX文件后缀索引
	public static final int XLSX_INDEX = 2;

	/**
	 * 读文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static List<List<Object>> read(String filePath) {
		List<List<Object>> list = null;
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toLowerCase();

		// 判断文件后缀名
		switch (fileType) {
		case XLS_NAME:
			list = readXLS(filePath);
			break;
		case XLSX_NAME:
			list = readXLSX(filePath);
			break;
		default:
			logger.error("File format error");
			break;
		}

		return list;
	}

	/**
	 * 写文件
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
	public static boolean write(String filePath, String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
		boolean success = false;
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toLowerCase();

		// 判断文件后缀名
		if (fileType.equalsIgnoreCase(XLS_NAME)
				|| fileType.equalsIgnoreCase(XLSX_NAME)) {
			String dir = filePath.substring(0, filePath.lastIndexOf("\\"));

			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}

			switch (fileType) {
			case XLS_NAME:
				writeXLS(filePath, sheetName, titleName, titles, datas, checks);
				break;
			default:
				writeXLSX(filePath, sheetName, titleName, titles, datas, checks);
				break;
			}

			success = true;
		} else {
			logger.error("File format error");
		}

		return success;
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param suffixIndex
	 *            文件后缀索引(1:xls文件,2:xlsx文件)
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
	public static byte[] write(int suffixIndex, String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
		byte[] byt = null;

		// 判断文件后缀索引
		switch (suffixIndex) {
		case XLS_INDEX:
			byt = writeXLS(sheetName, titleName, titles, datas, checks);
			break;
		case XLSX_INDEX:
			byt = writeXLSX(sheetName, titleName, titles, datas, checks);
			break;
		default:
			logger.error("File format error");
			break;
		}

		return byt;
	}

	/**
	 * 读XLS文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	private static List<List<Object>> readXLS(String filePath) {
		List<List<Object>> list = null;
		InputStream inputStream = null;
		HSSFWorkbook workbook = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			workbook = new HSSFWorkbook(inputStream);

			// Excel的页签数量
			int sheetNum = workbook.getNumberOfSheets();
			if (sheetNum > 0) {
				list = new ArrayList<List<Object>>();

				for (int i = 0; i < sheetNum; i++) {
					List<Object> ls = new ArrayList<Object>();
					HSSFSheet sheet = workbook.getSheetAt(i);

					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();

						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							if (cell.getCellType() == CellType.NUMERIC) {
								ls.add(cell.getNumericCellValue());
							} else if (cell.getCellType() == CellType.BOOLEAN) {
								ls.add(cell.getBooleanCellValue());
							} else {
								ls.add(cell.getStringCellValue());
							}
						}
					}

					list.add(ls);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read XLS file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}

				if (StringUtils.isNotEmpty(workbook)) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 读XLSX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	private static List<List<Object>> readXLSX(String filePath) {
		List<List<Object>> list = null;
		InputStream inputStream = null;
		XSSFWorkbook workbook = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			workbook = new XSSFWorkbook(inputStream);

			// Excel的页签数量
			int sheetNum = workbook.getNumberOfSheets();
			if (sheetNum > 0) {
				list = new ArrayList<List<Object>>();

				for (int i = 0; i < sheetNum; i++) {
					List<Object> ls = new ArrayList<Object>();
					XSSFSheet sheet = workbook.getSheetAt(i);

					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();

						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							if (cell.getCellType() == CellType.NUMERIC) {
								ls.add(cell.getNumericCellValue());
							} else if (cell.getCellType() == CellType.BOOLEAN) {
								ls.add(cell.getBooleanCellValue());
							} else {
								ls.add(cell.getStringCellValue());
							}
						}
					}

					list.add(ls);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read XLSX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}

				if (StringUtils.isNotEmpty(workbook)) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 写XLS文件
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
	private static void writeXLS(String filePath, String sheetName,
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
									Iterator<Entry<String, Number>> checkIterator = checks
											.entrySet().iterator();

									while (checkIterator.hasNext()) {
										Entry<String, Number> checkEntry = checkIterator
												.next();

										if (key.indexOf(checkEntry.getKey()) != -1) {
											isCheck = true;
											value = checkEntry.getValue();
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
			logger.error("Write XLS file error");
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
	 * 写XLSX文件
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
	private static void writeXLSX(String filePath, String sheetName,
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
									Iterator<Entry<String, Number>> checkIterator = checks
											.entrySet().iterator();

									while (checkIterator.hasNext()) {
										Entry<String, Number> checkEntry = checkIterator
												.next();

										if (key.indexOf(checkEntry.getKey()) != -1) {
											isCheck = true;
											value = checkEntry.getValue();
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
			logger.error("Write XLSX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}

				if (StringUtils.isNotEmpty(workbook)) {
					workbook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写XLS文件(Byte)
	 * 
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
	@SuppressWarnings("resource")
	private static byte[] writeXLS(String sheetName, String titleName,
			List<String> titles, List<Map<String, Object>> datas,
			Map<String, Number> checks) {
		ByteArrayOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		byte[] byt = null;

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
									Iterator<Entry<String, Number>> checkIterator = checks
											.entrySet().iterator();

									while (checkIterator.hasNext()) {
										Entry<String, Number> checkEntry = checkIterator
												.next();

										if (key.indexOf(checkEntry.getKey()) != -1) {
											isCheck = true;
											value = checkEntry.getValue();
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

			outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byt = outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLS file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return byt;
	}

	/**
	 * 写XLSX文件(Byte)
	 * 
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
	private static byte[] writeXLSX(String sheetName, String titleName,
			List<String> titles, List<Map<String, Object>> datas,
			Map<String, Number> checks) {
		ByteArrayOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		byte[] byt = null;

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
									Iterator<Entry<String, Number>> checkIterator = checks
											.entrySet().iterator();

									while (checkIterator.hasNext()) {
										Entry<String, Number> checkEntry = checkIterator
												.next();

										if (key.indexOf(checkEntry.getKey()) != -1) {
											isCheck = true;
											value = checkEntry.getValue();
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

			outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byt = outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLSX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}

				if (StringUtils.isNotEmpty(workbook)) {
					workbook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return byt;
	}
}