package com.maven.common.xls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;
import com.maven.common.date.DateUtils;
import com.maven.common.xls.map.CellEntity;
import com.maven.common.xls.map.RowEntity;
import com.maven.common.xls.map.SheetEntity;
import com.maven.common.xls.map.StyleEntity;
import com.maven.common.xls.map.TitleEntity;
import com.maven.common.xls.map.XLSEntity;

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
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param xlsEntity
	 *            XLS数据
	 * @return
	 */
	public static boolean write(String filePath, XLSEntity xlsEntity) {
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
				writeXLS(filePath, xlsEntity);
				break;
			default:
				writeXLSX(filePath, xlsEntity);
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
	 * @param xlsEntity
	 *            XLS数据
	 * @return
	 */
	public static byte[] write(int suffixIndex, XLSEntity xlsEntity) {
		byte[] byt = null;

		// 判断文件后缀索引
		switch (suffixIndex) {
		case XLS_INDEX:
			byt = writeXLS(xlsEntity);
			break;
		case XLSX_INDEX:
			byt = writeXLSX(xlsEntity);
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

		try {
			workbook = getHSSFWorkbook(sheetName, titleName, titles, datas,
					checks);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
			}
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

		try {
			workbook = new XSSFWorkbook();

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
			}
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
	private static byte[] writeXLS(String sheetName, String titleName,
			List<String> titles, List<Map<String, Object>> datas,
			Map<String, Number> checks) {
		ByteArrayOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		byte[] byt = null;

		try {
			workbook = getHSSFWorkbook(sheetName, titleName, titles, datas,
					checks);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				byt = outputStream.toByteArray();
			}
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
		byte[] byt = null;

		try {
			workbook = getXSSFWorkbook(sheetName, titleName, titles, datas,
					checks);

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				byt = outputStream.toByteArray();
			}
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

	/**
	 * 写XLS文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param xlsEntity
	 *            XLS数据
	 */
	private static void writeXLS(String filePath, XLSEntity xlsEntity) {
		FileOutputStream outputStream = null;
		HSSFWorkbook workbook = null;

		try {
			workbook = getHSSFWorkbook(xlsEntity);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
			}
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
	 * @param xlsEntity
	 *            XLS数据
	 */
	private static void writeXLSX(String filePath, XLSEntity xlsEntity) {
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;

		try {
			workbook = getXSSFWorkbook(xlsEntity);

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
			}
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
	 * 写XLS文件(Byte)
	 * 
	 * @param xlsEntity
	 *            XLS数据
	 */
	private static byte[] writeXLS(XLSEntity xlsEntity) {
		ByteArrayOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		byte[] byt = null;

		try {
			workbook = getHSSFWorkbook(xlsEntity);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				byt = outputStream.toByteArray();
			}
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
	 * @param xlsEntity
	 *            XLS数据
	 */
	private static byte[] writeXLSX(XLSEntity xlsEntity) {
		ByteArrayOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		byte[] byt = null;

		try {
			workbook = getXSSFWorkbook(xlsEntity);

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				byt = outputStream.toByteArray();
			}
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
	 * 获取HSSFWorkbook
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
	 * @return
	 */
	private static HSSFWorkbook getHSSFWorkbook(String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLS file error");
		}

		return workbook;
	}

	/**
	 * 获取HSSFWorkbook
	 * 
	 * @param xlsEntity
	 *            XLS数据
	 * @return
	 */
	private static HSSFWorkbook getHSSFWorkbook(XLSEntity xlsEntity) {
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cellStyle = null;
		HSSFFont font = null;
		CellRangeAddress region = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		try {
			// 判断XLS数据
			if (StringUtils.isNotEmpty(xlsEntity)) {
				workbook = new HSSFWorkbook();

				List<StyleEntity> styleList = xlsEntity.getStyles();
				List<SheetEntity> sheetList = xlsEntity.getSheets();
				Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();

				// 判断是否有样式
				if (styleList.size() > 0) {
					// 遍历样式
					for (StyleEntity sEntity : styleList) {
						cellStyle = workbook.createCellStyle();
						String styleName = sEntity.getName();
						short fontSize = sEntity.getFontSize();
						short fontColor = sEntity.getFontColor();
						boolean bold = sEntity.getBold();
						boolean italic = sEntity.getItalic();

						// 判断是否有字体大小、字体颜色、加粗或斜体
						if (fontSize > 0 || fontColor > 0 || bold || italic) {
							font = workbook.createFont();

							if (fontSize > 0) {
								font.setFontHeightInPoints(fontSize);
							}

							if (fontColor > 0) {
								font.setColor(fontColor);
							}

							if (bold) {
								font.setBold(bold);
							}

							if (italic) {
								font.setItalic(italic);
							}

							cellStyle.setFont(font);
						}

						HorizontalAlignment horizontal = HorizontalAlignment.CENTER;
						VerticalAlignment vertical = VerticalAlignment.CENTER;

						// 设置水平位置
						switch (sEntity.getHorizontal()) {
						case 1:
							horizontal = HorizontalAlignment.LEFT;
							break;
						case 3:
							horizontal = HorizontalAlignment.RIGHT;
							break;
						}

						// 设置垂直位置
						switch (sEntity.getVertical()) {
						case 1:
							vertical = VerticalAlignment.TOP;
							break;
						case 3:
							vertical = VerticalAlignment.BOTTOM;
							break;
						case 4:
							vertical = VerticalAlignment.JUSTIFY;
							break;
						case 5:
							vertical = VerticalAlignment.DISTRIBUTED;
							break;
						}

						cellStyle.setAlignment(horizontal);
						cellStyle.setVerticalAlignment(vertical);
						cellStyle.setBorderLeft(BorderStyle.THIN);
						cellStyle.setBorderTop(BorderStyle.THIN);
						cellStyle.setBorderRight(BorderStyle.THIN);
						cellStyle.setBorderBottom(BorderStyle.THIN);

						styleMap.put(styleName, cellStyle);
					}
				}

				// 判断是否有数据
				if (sheetList.size() > 0) {
					// 遍历数据
					for (SheetEntity shEntity : sheetList) {
						String sheetName = shEntity.getName();
						List<TitleEntity> titleList = shEntity.getTitles();
						List<RowEntity> rowList = shEntity.getRows();

						int rowIndex = 0;
						sheet = workbook.createSheet(sheetName);

						// 判断是否有合并的标题数据
						if (titleList.size() > 0) {
							int regionNum = rowList.size() > 0 ? rowList.get(0)
									.getCells().size() : 0;

							// 遍历标题数据
							for (TitleEntity tEntity : titleList) {
								row = sheet.createRow(rowIndex);
								row.setHeight(tEntity.getHeight());
								cell = row.createCell(0);
								cell.setCellValue(tEntity.getName());

								cellStyle = styleMap
										.get(tEntity.getStyleName());
								if (StringUtils.isNotEmpty(cellStyle)) {
									cell.setCellStyle(cellStyle);
								}

								region = new CellRangeAddress(0, 0, 0,
										regionNum - 1);
								sheet.addMergedRegion(region);

								RegionUtil.setBorderLeft(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderTop(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderRight(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderBottom(BorderStyle.THIN,
										region, sheet);

								rowIndex += 1;
							}
						}

						// 判断是否有数据
						if (rowList.size() > 0) {
							boolean setColumn = true;

							// 遍历数据
							for (RowEntity rEntity : rowList) {
								List<CellEntity> cellList = rEntity.getCells();

								row = sheet.createRow(rowIndex);
								row.setHeight(rEntity.getHeight());

								for (int i = 0; i < cellList.size(); i++) {
									CellEntity cEntity = cellList.get(i);
									Object value = cEntity.getValue();
									int width = cEntity.getWidth();
									String styleName = cEntity.getStyleName();

									cell = row.createCell(i);
									CellType cellType = CellType.STRING;

									if (value instanceof String) {
										cell.setCellValue(value.toString());
									} else if (value instanceof Number) {
										cellType = CellType.NUMERIC;
										cell.setCellValue(Double
												.parseDouble(value.toString()));
									} else if (value instanceof Boolean) {
										cellType = CellType.BOOLEAN;
										cell.setCellValue(Boolean
												.parseBoolean(value.toString()));
									} else if (value instanceof Date) {
										cell.setCellValue(DateUtils
												.dateToStr((Date) value));
									}

									cell.setCellType(cellType);

									cellStyle = styleMap.get(styleName);
									if (StringUtils.isNotEmpty(cellStyle)) {
										cell.setCellStyle(cellStyle);
									}

									if (setColumn) {
										sheet.setColumnWidth(i, width);
									}
								}

								setColumn = false;
								rowIndex += 1;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLS file error");
		}

		return workbook;
	}

	/**
	 * 获取XSSFWorkbook
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
	 * @return
	 */
	private static XSSFWorkbook getXSSFWorkbook(String sheetName,
			String titleName, List<String> titles,
			List<Map<String, Object>> datas, Map<String, Number> checks) {
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLSX file error");
		}

		return workbook;
	}

	/**
	 * 获取XSSFWorkbook
	 * 
	 * @param xlsEntity
	 *            xls数据
	 * @return
	 */
	private static XSSFWorkbook getXSSFWorkbook(XLSEntity xlsEntity) {
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFCellStyle cellStyle = null;
		XSSFFont font = null;
		CellRangeAddress region = null;
		XSSFRow row = null;
		XSSFCell cell = null;

		try {
			// 判断XLS数据
			if (StringUtils.isNotEmpty(xlsEntity)) {
				workbook = new XSSFWorkbook();

				List<StyleEntity> styleList = xlsEntity.getStyles();
				List<SheetEntity> sheetList = xlsEntity.getSheets();
				Map<String, XSSFCellStyle> styleMap = new HashMap<String, XSSFCellStyle>();

				// 判断是否有样式
				if (styleList.size() > 0) {
					// 遍历样式
					for (StyleEntity sEntity : styleList) {
						cellStyle = workbook.createCellStyle();
						String styleName = sEntity.getName();
						short fontSize = sEntity.getFontSize();
						short fontColor = sEntity.getFontColor();
						boolean bold = sEntity.getBold();
						boolean italic = sEntity.getItalic();

						// 判断是否有字体大小、字体颜色、加粗或斜体
						if (fontSize > 0 || fontColor > 0 || bold || italic) {
							font = workbook.createFont();

							if (fontSize > 0) {
								font.setFontHeightInPoints(fontSize);
							}

							if (fontColor > 0) {
								font.setColor(fontColor);
							}

							if (bold) {
								font.setBold(bold);
							}

							if (italic) {
								font.setItalic(italic);
							}

							cellStyle.setFont(font);
						}

						HorizontalAlignment horizontal = HorizontalAlignment.CENTER;
						VerticalAlignment vertical = VerticalAlignment.CENTER;

						// 设置水平位置
						switch (sEntity.getHorizontal()) {
						case 1:
							horizontal = HorizontalAlignment.LEFT;
							break;
						case 3:
							horizontal = HorizontalAlignment.RIGHT;
							break;
						}

						// 设置垂直位置
						switch (sEntity.getVertical()) {
						case 1:
							vertical = VerticalAlignment.TOP;
							break;
						case 3:
							vertical = VerticalAlignment.BOTTOM;
							break;
						case 4:
							vertical = VerticalAlignment.JUSTIFY;
							break;
						case 5:
							vertical = VerticalAlignment.DISTRIBUTED;
							break;
						}

						cellStyle.setAlignment(horizontal);
						cellStyle.setVerticalAlignment(vertical);
						cellStyle.setBorderLeft(BorderStyle.THIN);
						cellStyle.setBorderTop(BorderStyle.THIN);
						cellStyle.setBorderRight(BorderStyle.THIN);
						cellStyle.setBorderBottom(BorderStyle.THIN);

						styleMap.put(styleName, cellStyle);
					}
				}

				// 判断是否有数据
				if (sheetList.size() > 0) {
					// 遍历数据
					for (SheetEntity shEntity : sheetList) {
						String sheetName = shEntity.getName();
						List<TitleEntity> titleList = shEntity.getTitles();
						List<RowEntity> rowList = shEntity.getRows();

						int rowIndex = 0;
						sheet = workbook.createSheet(sheetName);

						// 判断是否有合并的标题数据
						if (titleList.size() > 0) {
							int regionNum = rowList.size() > 0 ? rowList.get(0)
									.getCells().size() : 0;

							// 遍历标题数据
							for (TitleEntity tEntity : titleList) {
								row = sheet.createRow(rowIndex);
								row.setHeight(tEntity.getHeight());
								cell = row.createCell(0);
								cell.setCellValue(tEntity.getName());

								cellStyle = styleMap
										.get(tEntity.getStyleName());
								if (StringUtils.isNotEmpty(cellStyle)) {
									cell.setCellStyle(cellStyle);
								}

								region = new CellRangeAddress(0, 0, 0,
										regionNum - 1);
								sheet.addMergedRegion(region);

								RegionUtil.setBorderLeft(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderTop(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderRight(BorderStyle.THIN,
										region, sheet);
								RegionUtil.setBorderBottom(BorderStyle.THIN,
										region, sheet);

								rowIndex += 1;
							}
						}

						// 判断是否有数据
						if (rowList.size() > 0) {
							boolean setColumn = true;

							// 遍历数据
							for (RowEntity rEntity : rowList) {
								List<CellEntity> cellList = rEntity.getCells();

								row = sheet.createRow(rowIndex);
								row.setHeight(rEntity.getHeight());

								for (int i = 0; i < cellList.size(); i++) {
									CellEntity cEntity = cellList.get(i);
									Object value = cEntity.getValue();
									int width = cEntity.getWidth();
									String styleName = cEntity.getStyleName();

									cell = row.createCell(i);
									CellType cellType = CellType.STRING;

									if (value instanceof String) {
										cell.setCellValue(value.toString());
									} else if (value instanceof Number) {
										cellType = CellType.NUMERIC;
										cell.setCellValue(Double
												.parseDouble(value.toString()));
									} else if (value instanceof Boolean) {
										cellType = CellType.BOOLEAN;
										cell.setCellValue(Boolean
												.parseBoolean(value.toString()));
									} else if (value instanceof Date) {
										cell.setCellValue(DateUtils
												.dateToStr((Date) value));
									}

									cell.setCellType(cellType);

									cellStyle = styleMap.get(styleName);
									if (StringUtils.isNotEmpty(cellStyle)) {
										cell.setCellStyle(cellStyle);
									}

									if (setColumn) {
										sheet.setColumnWidth(i, width);
									}
								}

								setColumn = false;
								rowIndex += 1;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XLS file error");
		}

		return workbook;
	}

	public static void main(String[] args) {
		XLSEntity xlsEntity = new XLSEntity();
		List<StyleEntity> styles = new ArrayList<StyleEntity>();
		List<SheetEntity> sheets = new ArrayList<SheetEntity>();

		StyleEntity styleEntity = new StyleEntity("top");
		styleEntity.setBold(true);
		styleEntity.setFontSize((short) 13);
		styles.add(styleEntity);
		styleEntity = new StyleEntity("title");
		styleEntity.setBold(true);
		styles.add(styleEntity);
		styleEntity = new StyleEntity("content");
		styles.add(styleEntity);

		SheetEntity sheetEntity = new SheetEntity("学生信息");
		List<TitleEntity> titles = new ArrayList<TitleEntity>();
		List<RowEntity> rows = new ArrayList<RowEntity>();
		TitleEntity titleEntity = new TitleEntity("学生成绩表");
		titleEntity.setStyleName("top");
		titles.add(titleEntity);
		RowEntity rowEntity = new RowEntity();
		List<CellEntity> cells = new ArrayList<CellEntity>();
		CellEntity cellEntity = new CellEntity("姓名");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("性别");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("年龄");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("语文");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("数学");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("英语");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		cellEntity = new CellEntity("时间");
		cellEntity.setStyleName("title");
		cells.add(cellEntity);
		rowEntity.setCells(cells);
		rows.add(rowEntity);
		rowEntity = new RowEntity();
		cells = new ArrayList<CellEntity>();
		cellEntity = new CellEntity("梅西");
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity("男");
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity(20);
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity(78.5);
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity(99);
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity(88.7);
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		cellEntity = new CellEntity(new Date());
		cellEntity.setStyleName("content");
		cells.add(cellEntity);
		rowEntity.setCells(cells);
		rows.add(rowEntity);
		sheetEntity.setTitles(titles);
		sheetEntity.setRows(rows);
		sheets.add(sheetEntity);

		xlsEntity.setStyles(styles);
		xlsEntity.setSheets(sheets);

		write("F:\\1.xlsx", xlsEntity);
	}
}