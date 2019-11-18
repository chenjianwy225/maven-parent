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
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;
import com.maven.common.date.DateUtils;
import com.maven.common.xls.entity.CellEntity;
import com.maven.common.xls.entity.RowEntity;
import com.maven.common.xls.entity.SheetEntity;
import com.maven.common.xls.entity.StyleEntity;
import com.maven.common.xls.entity.TextListEntity;
import com.maven.common.xls.entity.TitleEntity;
import com.maven.common.xls.entity.XLSEntity;

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
	 * @return JSONArray对象
	 */
	public static JSONArray read(String filePath) {
		JSONArray jsonArray = null;
		String message = "Parameter error";

		// 判断传入参数
		if (StringUtils.isNotEmpty(filePath)) {
			message = "File not exist";

			File file = new File(filePath);

			// 判断文件是否存在
			if (file.exists()) {
				String fileType = filePath.substring(
						filePath.lastIndexOf(".") + 1).toLowerCase();

				message = "Read XLS/XLSX file failure";
				// 判断文件后缀名
				switch (fileType) {
				case XLS_NAME:
					jsonArray = readXLS(filePath);
					break;
				case XLSX_NAME:
					jsonArray = readXLSX(filePath);
					break;
				default:
					message = "File format error";
					break;
				}

				// 判断是否读取成功
				if (StringUtils.isNotEmpty(jsonArray)) {
					message = "Read XLS/XLSX file success";
				}
			}
		}

		logger.info(message);

		return jsonArray;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return 是否写入成功
	 */
	public static boolean write(String filePath, JSONObject jsonObject) {
		boolean result = false;
		String message = "Parameter error";

		// 判断传入参数
		if (StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(jsonObject)) {
			message = "File format error";

			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
					.toLowerCase();

			// 判断文件后缀名
			if (fileType.equalsIgnoreCase(XLS_NAME)
					|| fileType.equalsIgnoreCase(XLSX_NAME)) {
				String dir = filePath.substring(0, filePath.lastIndexOf("\\"));

				// 判断文件夹是否存在
				File file = new File(dir);
				if (!file.exists()) {
					file.mkdirs();
				}

				message = "Write XLS/XLSX file failure";
				// 判断文件后缀类型
				switch (fileType) {
				case XLS_NAME:
					result = writeXLS(filePath, jsonObject);
					break;
				default:
					result = writeXLSX(filePath, jsonObject);
					break;
				}

				// 判断是否写入成功
				if (result) {
					message = "Write XLS/XLSX file success";
				}
			}
		}

		logger.info(message);

		return result;
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param suffixIndex
	 *            文件后缀索引(1:xls文件,2:xlsx文件)
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return byte数组
	 */
	public static byte[] write(int suffixIndex, JSONObject jsonObject) {
		byte[] byt = null;
		String message = "Parameter error";

		// 判断传入参数
		if (suffixIndex > 0 && StringUtils.isNotEmpty(jsonObject)) {
			message = "Write XLS/XLSX file failure";

			// 判断文件后缀索引
			switch (suffixIndex) {
			case XLS_INDEX:
				byt = writeXLS(jsonObject);
				break;
			case XLSX_INDEX:
				byt = writeXLSX(jsonObject);
				break;
			default:
				message = "File format error";
				break;
			}

			// 判断是否写入成功
			if (StringUtils.isNotEmpty(byt)) {
				message = "Write XLS/XLSX file success";
			}
		}

		logger.info(message);

		return byt;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param xlsEntity
	 *            XLS数据
	 * @return 是否写入成功
	 */
	public static boolean write(String filePath, XLSEntity xlsEntity) {
		boolean result = false;
		String message = "Parameter error";

		// 判断传入参数
		if (StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(xlsEntity)) {
			message = "File format error";

			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
					.toLowerCase();

			// 判断文件后缀名
			if (fileType.equalsIgnoreCase(XLS_NAME)
					|| fileType.equalsIgnoreCase(XLSX_NAME)) {
				String dir = filePath.substring(0, filePath.lastIndexOf("\\"));

				// 判断文件夹是否存在
				File file = new File(dir);
				if (!file.exists()) {
					file.mkdirs();
				}

				message = "Write XLS/XLSX file failure";
				// 判断文件后缀类型
				switch (fileType) {
				case XLS_NAME:
					result = writeXLS(filePath, xlsEntity);
					break;
				default:
					result = writeXLSX(filePath, xlsEntity);
					break;
				}

				// 判断是否写入成功
				if (result) {
					message = "Write XLS/XLSX file success";
				}
			}
		}

		logger.info(message);

		return result;
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param suffixIndex
	 *            文件后缀索引(1:xls文件,2:xlsx文件)
	 * @param xlsEntity
	 *            XLS数据
	 * @return byte数组
	 */
	public static byte[] write(int suffixIndex, XLSEntity xlsEntity) {
		byte[] byt = null;
		String message = "Parameter error";

		// 判断传入参数
		if (suffixIndex > 0 && StringUtils.isNotEmpty(xlsEntity)) {
			message = "Write XLS/XLSX file failure";

			// 判断文件后缀索引
			switch (suffixIndex) {
			case XLS_INDEX:
				byt = writeXLS(xlsEntity);
				break;
			case XLSX_INDEX:
				byt = writeXLSX(xlsEntity);
				break;
			default:
				message = "File format error";
				break;
			}

			// 判断是否写入成功
			if (StringUtils.isNotEmpty(byt)) {
				message = "Write XLS/XLSX file success";
			}
		}

		logger.info(message);

		return byt;
	}

	/**
	 * 读XLS文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONArray对象
	 */
	private static JSONArray readXLS(String filePath) {
		JSONArray jsonArray = null;
		InputStream inputStream = null;
		HSSFWorkbook workbook = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			workbook = new HSSFWorkbook(inputStream);

			// Excel的页签数量
			int sheetNum = workbook.getNumberOfSheets();
			if (sheetNum > 0) {
				jsonArray = new JSONArray();

				for (int i = 0; i < sheetNum; i++) {
					JSONObject object = new JSONObject();
					JSONArray array = new JSONArray();
					HSSFSheet sheet = workbook.getSheetAt(i);

					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext()) {
						JSONArray datas = new JSONArray();
						Row row = rowIterator.next();

						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							if (cell.getCellType() == CellType.NUMERIC) {
								datas.add(cell.getNumericCellValue());
							} else if (cell.getCellType() == CellType.BOOLEAN) {
								datas.add(cell.getBooleanCellValue());
							} else {
								datas.add(cell.getStringCellValue());
							}
						}

						array.add(datas);
					}

					object.put("sheetName", sheet.getSheetName());
					object.put("datas", array);
					jsonArray.add(object);
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

		return jsonArray;
	}

	/**
	 * 读XLSX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONArray对象
	 */
	private static JSONArray readXLSX(String filePath) {
		JSONArray jsonArray = null;
		InputStream inputStream = null;
		XSSFWorkbook workbook = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			workbook = new XSSFWorkbook(inputStream);

			// Excel的页签数量
			int sheetNum = workbook.getNumberOfSheets();
			if (sheetNum > 0) {
				jsonArray = new JSONArray();

				for (int i = 0; i < sheetNum; i++) {
					JSONObject object = new JSONObject();
					JSONArray array = new JSONArray();
					XSSFSheet sheet = workbook.getSheetAt(i);

					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext()) {
						JSONArray datas = new JSONArray();
						Row row = rowIterator.next();

						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							if (cell.getCellType() == CellType.NUMERIC) {
								datas.add(cell.getNumericCellValue());
							} else if (cell.getCellType() == CellType.BOOLEAN) {
								datas.add(cell.getBooleanCellValue());
							} else {
								datas.add(cell.getStringCellValue());
							}
						}

						array.add(datas);
					}

					object.put("sheetName", sheet.getSheetName());
					object.put("datas", array);
					jsonArray.add(object);
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

		return jsonArray;
	}

	/**
	 * 写XLS文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return 是否写入成功
	 */
	private static boolean writeXLS(String filePath, JSONObject jsonObject) {
		boolean result = false;
		FileOutputStream outputStream = null;
		HSSFWorkbook workbook = null;

		try {
			workbook = getHSSFWorkbook(jsonObject);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);

				result = true;
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

		return result;
	}

	/**
	 * 写XLSX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return 是否写入成功
	 */
	private static boolean writeXLSX(String filePath, JSONObject jsonObject) {
		boolean result = false;
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;

		try {
			workbook = getXSSFWorkbook(jsonObject);

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);

				result = true;
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

		return result;
	}

	/**
	 * 写XLS文件(Byte)
	 * 
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return byte数组
	 */
	private static byte[] writeXLS(JSONObject jsonObject) {
		ByteArrayOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		byte[] byt = null;

		try {
			workbook = getHSSFWorkbook(jsonObject);

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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return byte数组
	 */
	private static byte[] writeXLSX(JSONObject jsonObject) {
		ByteArrayOutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		byte[] byt = null;

		try {
			workbook = getXSSFWorkbook(jsonObject);

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
	 * @return 是否写入成功
	 */
	private static boolean writeXLS(String filePath, XLSEntity xlsEntity) {
		boolean result = false;
		FileOutputStream outputStream = null;
		HSSFWorkbook workbook = null;

		try {
			workbook = getHSSFWorkbook(xlsEntity);

			// 判断HSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);

				result = true;
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

		return result;
	}

	/**
	 * 写XLSX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param xlsEntity
	 *            XLS数据
	 * @return 是否写入成功
	 */
	private static boolean writeXLSX(String filePath, XLSEntity xlsEntity) {
		boolean result = false;
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;

		try {
			workbook = getXSSFWorkbook(xlsEntity);

			// 判断XSSFWorkbook
			if (StringUtils.isNotEmpty(workbook)) {
				outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);

				result = true;
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

		return result;
	}

	/**
	 * 写XLS文件(Byte)
	 * 
	 * @param xlsEntity
	 *            XLS数据
	 * @return byte数组
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
	 * @return byte数组
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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return HSSFWorkbook对象
	 */
	private static HSSFWorkbook getHSSFWorkbook(JSONObject jsonObject) {
		HSSFWorkbook workbook = null;

		try {
			// 判断数据对象
			if (StringUtils.isNotEmpty(jsonObject)) {
				JSONArray styles = jsonObject.getJSONArray("styles");
				JSONArray sheets = jsonObject.getJSONArray("sheets");
				XLSEntity xlsEntity = new XLSEntity();

				// 判断是否有样式
				if (StringUtils.isNotEmpty(styles) && styles.size() > 0) {
					List<StyleEntity> styleList = new ArrayList<StyleEntity>();

					// 遍历样式
					for (int i = 0; i < styles.size(); i++) {
						JSONObject style = styles.getJSONObject(i);
						String styleName = style.getString("name");
						int horizontal = style.getIntValue("horizontal");
						int vertical = style.getIntValue("vertical");
						String fontName = style.getString("fontName");
						short fontSize = style.getShortValue("fontSize");
						short fontColor = style.getShortValue("fontColor");
						int underline = style.getIntValue("underline");
						int typeOffset = style.getIntValue("typeOffset");
						short backgroundColor = style
								.getShortValue("backgroundColor");
						boolean bold = style.getBooleanValue("bold");
						boolean italic = style.getBooleanValue("italic");
						boolean strikeout = style.getBooleanValue("strikeout");

						StyleEntity styleEntity = new StyleEntity(styleName);

						if (horizontal > 0) {
							styleEntity.setHorizontal(horizontal);
						}

						if (vertical > 0) {
							styleEntity.setVertical(vertical);
						}

						if (StringUtils.isNotEmpty(fontName)) {
							styleEntity.setFontName(fontName);
						}

						if (fontSize > 0) {
							styleEntity.setFontSize(fontSize);
						}

						if (fontColor > 0) {
							styleEntity.setFontColor(fontColor);
						}

						if (underline > 0) {
							styleEntity.setUnderline(underline);
						}

						if (typeOffset > 0) {
							styleEntity.setTypeOffset(typeOffset);
						}

						if (backgroundColor > 0) {
							styleEntity.setBackgroundColor(backgroundColor);
						}

						styleEntity.setBold(bold);
						styleEntity.setItalic(italic);
						styleEntity.setStrikeout(strikeout);

						styleList.add(styleEntity);
					}

					xlsEntity.setStyles(styleList);
				}

				// 判断是否有工作簿
				if (StringUtils.isNotEmpty(sheets) && sheets.size() > 0) {
					List<SheetEntity> sheetList = new ArrayList<SheetEntity>();

					// 遍历工作簿
					for (int i = 0; i < sheets.size(); i++) {
						JSONObject sheet = sheets.getJSONObject(i);
						String sheetName = sheet.getString("name");
						JSONArray texts = sheet.getJSONArray("texts");
						JSONArray titles = sheet.getJSONArray("titles");
						JSONArray rows = sheet.getJSONArray("rows");

						SheetEntity sheetEntity = new SheetEntity(sheetName);

						// 判断是否有下拉列表
						if (StringUtils.isNotEmpty(texts) && texts.size() > 0) {
							List<TextListEntity> textList = new ArrayList<TextListEntity>();

							// 遍历下拉列表
							for (int j = 0; j < texts.size(); j++) {
								JSONObject text = texts.getJSONObject(j);
								JSONArray datas = text.getJSONArray("datas");

								// 判断是否有下拉列表数据
								if (StringUtils.isNotEmpty(datas)
										&& datas.size() > 0) {
									int firstRow = text.getIntValue("firstRow");
									int lastRow = text.getIntValue("lastRow");
									int firstColumn = text
											.getIntValue("firstColumn");
									int lastColumn = text
											.getIntValue("lastColumn");

									TextListEntity textListEntity = new TextListEntity(
											datas.toJavaList(String.class),
											firstRow, lastRow, firstColumn,
											lastColumn);

									textList.add(textListEntity);
								}
							}

							sheetEntity.setTexts(textList);
						}

						// 判断是否有标题
						if (StringUtils.isNotEmpty(titles) && titles.size() > 0) {
							List<TitleEntity> titleList = new ArrayList<TitleEntity>();

							// 遍历标题
							for (int j = 0; j < titles.size(); j++) {
								JSONObject title = titles.getJSONObject(j);
								String titleName = title.getString("name");
								short height = title.getShortValue("height");
								String styleName = title.getString("styleName");

								TitleEntity titleEntity = new TitleEntity(
										titleName);
								titleEntity.setHeight(height);
								titleEntity.setStyleName(styleName);

								titleList.add(titleEntity);
							}

							sheetEntity.setTitles(titleList);
						}

						// 判断是否有行数据
						if (StringUtils.isNotEmpty(rows) && rows.size() > 0) {
							List<RowEntity> rowList = new ArrayList<RowEntity>();

							// 遍历行数据
							for (int j = 0; j < rows.size(); j++) {
								JSONObject row = rows.getJSONObject(j);
								short height = row.getShortValue("height");
								JSONArray cells = row.getJSONArray("cells");

								RowEntity rowEntity = new RowEntity();

								// 判断是否设置行高度
								if (height > 0) {
									rowEntity.setHeight(height);
								}

								// 判断是否单元格数据
								if (StringUtils.isNotEmpty(cells)
										&& cells.size() > 0) {
									List<CellEntity> cellList = new ArrayList<CellEntity>();

									// 遍历单元格数据
									for (int k = 0; k < cells.size(); k++) {
										JSONObject cell = cells
												.getJSONObject(k);
										int width = cell.getIntValue("width");
										Object value = cell.get("value");
										String styleName = cell
												.getString("styleName");

										CellEntity cellEntity = new CellEntity(
												value);
										cellEntity.setStyleName(styleName);

										// 判断是否设置单元格宽度
										if (width > 0) {
											cellEntity.setWidth(width);
										}

										cellList.add(cellEntity);
									}

									rowEntity.setCells(cellList);
								}

								rowList.add(rowEntity);
							}

							sheetEntity.setRows(rowList);
						}

						sheetList.add(sheetEntity);
					}

					xlsEntity.setSheets(sheetList);
				}

				workbook = getHSSFWorkbook(xlsEntity);
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
	 * @return HSSFWorkbook对象
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
						String fontName = sEntity.getFontName();
						short fontSize = sEntity.getFontSize();
						short fontColor = sEntity.getFontColor();
						int underline = sEntity.getUnderline();
						int typeOffset = sEntity.getTypeOffset();
						short backgroundColor = sEntity.getBackgroundColor();
						boolean bold = sEntity.getBold();
						boolean italic = sEntity.getItalic();
						boolean strikeout = sEntity.getStrikeout();

						// 判断是否有字体名称、字体大小、字体颜色、下划线、加粗、斜体或删除线
						if (StringUtils.isNotEmpty(fontName) || fontSize > 0
								|| fontColor > 0 || underline > 0
								|| typeOffset > 0 || backgroundColor > 0
								|| bold || italic || strikeout) {
							font = workbook.createFont();

							// 设置字体名称
							if (StringUtils.isNotEmpty(fontName)) {
								font.setFontName(fontName);
							}

							// 设置字体大小
							if (fontSize > 0) {
								font.setFontHeightInPoints(fontSize);
							}

							// 设置字体颜色
							if (fontColor > 0) {
								font.setColor(fontColor);
							}

							// 设置下划线
							if (underline > 0) {
								byte fontUnderline = Font.U_NONE;

								switch (sEntity.getUnderline()) {
								case 1:
									fontUnderline = Font.U_SINGLE;
									break;
								case 2:
									fontUnderline = Font.U_DOUBLE;
									break;
								case 3:
									fontUnderline = Font.U_SINGLE_ACCOUNTING;
									break;
								case 4:
									fontUnderline = Font.U_DOUBLE_ACCOUNTING;
									break;
								}

								font.setUnderline(fontUnderline);
							}

							// 设置上下标
							if (typeOffset > 0) {
								short fontFormatting = FontFormatting.SS_NONE;

								switch (typeOffset) {
								case 1:
									fontFormatting = FontFormatting.SS_SUPER;
									break;
								case 2:
									fontFormatting = FontFormatting.SS_SUB;
									break;
								}

								font.setTypeOffset(fontFormatting);
							}

							// 设置粗体
							if (bold) {
								font.setBold(bold);
							}

							// 设置斜体
							if (italic) {
								font.setItalic(italic);
							}

							// 设置删除线
							if (strikeout) {
								font.setStrikeout(strikeout);
							}

							cellStyle.setFont(font);
						}

						HorizontalAlignment horizontal = HorizontalAlignment.CENTER;
						VerticalAlignment vertical = VerticalAlignment.CENTER;

						// 设置水平位置
						switch (sEntity.getHorizontal()) {
						case 1:
							horizontal = HorizontalAlignment.GENERAL;
							break;
						case 2:
							horizontal = HorizontalAlignment.LEFT;
							break;
						case 4:
							horizontal = HorizontalAlignment.RIGHT;
							break;
						case 5:
							horizontal = HorizontalAlignment.FILL;
							break;
						case 6:
							horizontal = HorizontalAlignment.JUSTIFY;
							break;
						case 7:
							horizontal = HorizontalAlignment.CENTER_SELECTION;
							break;
						case 8:
							horizontal = HorizontalAlignment.DISTRIBUTED;
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

						// 设置背景颜色
						if (backgroundColor > 0) {
							cellStyle.setFillForegroundColor(backgroundColor);
							cellStyle
									.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
						List<TextListEntity> textList = shEntity.getTexts();
						List<TitleEntity> titleList = shEntity.getTitles();
						List<RowEntity> rowList = shEntity.getRows();

						int rowIndex = 0;
						sheet = workbook.createSheet(sheetName);

						// 判断是否有下拉列表数据
						if (textList.size() > 0) {
							// 遍历下拉列表数据
							for (TextListEntity textListEntity : textList) {
								String[] datas = new String[] {};

								// 设置下拉列表作用的单元格(firstRow、lastRow、firstColumn、lastColumn)
								CellRangeAddressList regions = new CellRangeAddressList(
										textListEntity.getFirstRow(),
										textListEntity.getLastRow(),
										textListEntity.getFirstColumn(),
										textListEntity.getLastColumn());
								// 生成并设置数据有效性验证
								DVConstraint constraint = DVConstraint
										.createExplicitListConstraint(textListEntity
												.getDatas().toArray(datas));
								HSSFDataValidation dataValidation = new HSSFDataValidation(
										regions, constraint);
								// 将有效性验证添加到表单
								sheet.addValidationData(dataValidation);
							}
						}

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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontName'-字体名称(String)
	 *            'fontSize'-字体大小(Short) 'fontColor'-字体颜色(Short)
	 *            'underline'-下划线(Integer) 'typeOffset'-上下标(Integer)
	 *            'backgroundColor'-背景颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) 'strikeout'-是否删除线(Boolean)
	 *            B、工作簿数据('name'-工作簿名称(String) 'texts'-下拉列表集合(JSONArray)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、下拉列表数据('datas'-数据集合(JSONArray) 'firstRow'-开始行(Integer)
	 *            'lastRow'-结束行(Integer) 'firstColumn'-开始列 'lastColumn'-结束列)
	 *            D、标题数据('name'-标题值(String) 'height'-标题高度
	 *            'styleName'-样式名称(String)) E、行数据('height'-行高度(Short)
	 *            'cells'-单元格集合(JSONArray)) F、单元格数据('width'-单元格宽度(Integer)
	 *            'value'-单元格值(Object) 'styleName'-样式名称(String))
	 * @return XSSFWorkbook对象
	 */
	private static XSSFWorkbook getXSSFWorkbook(JSONObject jsonObject) {
		XSSFWorkbook workbook = null;

		try {
			// 判断数据对象
			if (StringUtils.isNotEmpty(jsonObject)) {
				JSONArray styles = jsonObject.getJSONArray("styles");
				JSONArray sheets = jsonObject.getJSONArray("sheets");
				XLSEntity xlsEntity = new XLSEntity();

				// 判断是否有样式
				if (StringUtils.isNotEmpty(styles) && styles.size() > 0) {
					List<StyleEntity> styleList = new ArrayList<StyleEntity>();

					// 遍历样式
					for (int i = 0; i < styles.size(); i++) {
						JSONObject style = styles.getJSONObject(i);
						String styleName = style.getString("name");
						int horizontal = style.getIntValue("horizontal");
						int vertical = style.getIntValue("vertical");
						String fontName = style.getString("fontName");
						short fontSize = style.getShortValue("fontSize");
						short fontColor = style.getShortValue("fontColor");
						int underline = style.getIntValue("underline");
						int typeOffset = style.getIntValue("typeOffset");
						short backgroundColor = style
								.getShortValue("backgroundColor");
						boolean bold = style.getBooleanValue("bold");
						boolean italic = style.getBooleanValue("italic");
						boolean strikeout = style.getBooleanValue("strikeout");

						StyleEntity styleEntity = new StyleEntity(styleName);

						if (horizontal > 0) {
							styleEntity.setHorizontal(horizontal);
						}

						if (vertical > 0) {
							styleEntity.setVertical(vertical);
						}

						if (StringUtils.isNotEmpty(fontName)) {
							styleEntity.setFontName(fontName);
						}

						if (fontSize > 0) {
							styleEntity.setFontSize(fontSize);
						}

						if (fontColor > 0) {
							styleEntity.setFontColor(fontColor);
						}

						if (underline > 0) {
							styleEntity.setUnderline(underline);
						}

						if (typeOffset > 0) {
							styleEntity.setTypeOffset(typeOffset);
						}

						if (backgroundColor > 0) {
							styleEntity.setBackgroundColor(backgroundColor);
						}

						styleEntity.setBold(bold);
						styleEntity.setItalic(italic);
						styleEntity.setStrikeout(strikeout);

						styleList.add(styleEntity);
					}

					xlsEntity.setStyles(styleList);
				}

				// 判断是否有工作簿
				if (StringUtils.isNotEmpty(sheets) && sheets.size() > 0) {
					List<SheetEntity> sheetList = new ArrayList<SheetEntity>();

					// 遍历工作簿
					for (int i = 0; i < sheets.size(); i++) {
						JSONObject sheet = sheets.getJSONObject(i);
						String sheetName = sheet.getString("name");
						JSONArray texts = sheet.getJSONArray("texts");
						JSONArray titles = sheet.getJSONArray("titles");
						JSONArray rows = sheet.getJSONArray("rows");

						SheetEntity sheetEntity = new SheetEntity(sheetName);

						// 判断是否有下拉列表
						if (StringUtils.isNotEmpty(texts) && texts.size() > 0) {
							List<TextListEntity> textList = new ArrayList<TextListEntity>();

							// 遍历下拉列表
							for (int j = 0; j < texts.size(); j++) {
								JSONObject text = texts.getJSONObject(j);
								JSONArray datas = text.getJSONArray("datas");

								// 判断是否有下拉列表数据
								if (StringUtils.isNotEmpty(datas)
										&& datas.size() > 0) {
									int firstRow = text.getIntValue("firstRow");
									int lastRow = text.getIntValue("lastRow");
									int firstColumn = text
											.getIntValue("firstColumn");
									int lastColumn = text
											.getIntValue("lastColumn");

									TextListEntity textListEntity = new TextListEntity(
											datas.toJavaList(String.class),
											firstRow, lastRow, firstColumn,
											lastColumn);

									textList.add(textListEntity);
								}
							}

							sheetEntity.setTexts(textList);
						}

						// 判断是否有标题
						if (StringUtils.isNotEmpty(titles) && titles.size() > 0) {
							List<TitleEntity> titleList = new ArrayList<TitleEntity>();

							// 遍历标题
							for (int j = 0; j < titles.size(); j++) {
								JSONObject title = titles.getJSONObject(j);
								String titleName = title.getString("name");
								short height = title.getShortValue("height");
								String styleName = title.getString("styleName");

								TitleEntity titleEntity = new TitleEntity(
										titleName);
								titleEntity.setHeight(height);
								titleEntity.setStyleName(styleName);

								titleList.add(titleEntity);
							}

							sheetEntity.setTitles(titleList);
						}

						// 判断是否有行数据
						if (StringUtils.isNotEmpty(rows) && rows.size() > 0) {
							List<RowEntity> rowList = new ArrayList<RowEntity>();

							// 遍历行数据
							for (int j = 0; j < rows.size(); j++) {
								JSONObject row = rows.getJSONObject(j);
								short height = row.getShortValue("height");
								JSONArray cells = row.getJSONArray("cells");

								RowEntity rowEntity = new RowEntity();

								// 判断是否设置行高度
								if (height > 0) {
									rowEntity.setHeight(height);
								}

								// 判断是否单元格数据
								if (StringUtils.isNotEmpty(cells)
										&& cells.size() > 0) {
									List<CellEntity> cellList = new ArrayList<CellEntity>();

									// 遍历单元格数据
									for (int k = 0; k < cells.size(); k++) {
										JSONObject cell = cells
												.getJSONObject(k);
										int width = cell.getIntValue("width");
										Object value = cell.get("value");
										String styleName = cell
												.getString("styleName");

										CellEntity cellEntity = new CellEntity(
												value);
										cellEntity.setWidth(width);
										cellEntity.setStyleName(styleName);

										cellList.add(cellEntity);
									}

									rowEntity.setCells(cellList);
								}

								rowList.add(rowEntity);
							}

							sheetEntity.setRows(rowList);
						}

						sheetList.add(sheetEntity);
					}

					xlsEntity.setSheets(sheetList);
				}

				workbook = getXSSFWorkbook(xlsEntity);
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
	 * @return XSSFWorkbook对象
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
						String fontName = sEntity.getFontName();
						short fontSize = sEntity.getFontSize();
						short fontColor = sEntity.getFontColor();
						int underline = sEntity.getUnderline();
						int typeOffset = sEntity.getTypeOffset();
						short backgroundColor = sEntity.getBackgroundColor();
						boolean bold = sEntity.getBold();
						boolean italic = sEntity.getItalic();
						boolean strikeout = sEntity.getStrikeout();

						// 判断是否有字体名称、字体大小、字体颜色、下划线、加粗、斜体或删除线
						if (StringUtils.isNotEmpty(fontName) || fontSize > 0
								|| fontColor > 0 || underline > 0
								|| typeOffset > 0 || backgroundColor > 0
								|| bold || italic || strikeout) {
							font = workbook.createFont();

							// 设置字体名称
							if (StringUtils.isNotEmpty(fontName)) {
								font.setFontName(fontName);
							}

							// 设置字体大小
							if (fontSize > 0) {
								font.setFontHeightInPoints(fontSize);
							}

							// 设置字体颜色
							if (fontColor > 0) {
								font.setColor(fontColor);
							}

							// 设置下划线
							if (underline > 0) {
								byte fontUnderline = Font.U_NONE;

								switch (sEntity.getUnderline()) {
								case 1:
									fontUnderline = Font.U_SINGLE;
									break;
								case 2:
									fontUnderline = Font.U_DOUBLE;
									break;
								case 3:
									fontUnderline = Font.U_SINGLE_ACCOUNTING;
									break;
								case 4:
									fontUnderline = Font.U_DOUBLE_ACCOUNTING;
									break;
								}

								font.setUnderline(fontUnderline);
							}

							// 设置上下标
							if (typeOffset > 0) {
								short fontFormatting = FontFormatting.SS_NONE;

								switch (typeOffset) {
								case 1:
									fontFormatting = FontFormatting.SS_SUPER;
									break;
								case 2:
									fontFormatting = FontFormatting.SS_SUB;
									break;
								}

								font.setTypeOffset(fontFormatting);
							}

							// 设置粗体
							if (bold) {
								font.setBold(bold);
							}

							// 设置斜体
							if (italic) {
								font.setItalic(italic);
							}

							// 设置删除线
							if (strikeout) {
								font.setStrikeout(strikeout);
							}

							cellStyle.setFont(font);
						}

						HorizontalAlignment horizontal = HorizontalAlignment.CENTER;
						VerticalAlignment vertical = VerticalAlignment.CENTER;

						// 设置水平位置
						switch (sEntity.getHorizontal()) {
						case 1:
							horizontal = HorizontalAlignment.GENERAL;
							break;
						case 2:
							horizontal = HorizontalAlignment.LEFT;
							break;
						case 4:
							horizontal = HorizontalAlignment.RIGHT;
							break;
						case 5:
							horizontal = HorizontalAlignment.FILL;
							break;
						case 6:
							horizontal = HorizontalAlignment.JUSTIFY;
							break;
						case 7:
							horizontal = HorizontalAlignment.CENTER_SELECTION;
							break;
						case 8:
							horizontal = HorizontalAlignment.DISTRIBUTED;
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

						// 设置背景颜色
						if (backgroundColor > 0) {
							cellStyle.setFillForegroundColor(backgroundColor);
							cellStyle
									.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
						List<TextListEntity> textList = shEntity.getTexts();
						List<TitleEntity> titleList = shEntity.getTitles();
						List<RowEntity> rowList = shEntity.getRows();

						int rowIndex = 0;
						sheet = workbook.createSheet(sheetName);

						// 判断是否有下拉列表数据
						if (textList.size() > 0) {
							// 遍历下拉列表数据
							for (TextListEntity textListEntity : textList) {
								String[] datas = new String[] {};

								// 设置下拉列表作用的单元格(firstRow、lastRow、firstColumn、lastColumn)
								CellRangeAddressList regions = new CellRangeAddressList(
										textListEntity.getFirstRow(),
										textListEntity.getLastRow(),
										textListEntity.getFirstColumn(),
										textListEntity.getLastColumn());
								XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(
										sheet);
								XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) validationHelper
										.createExplicitListConstraint(textListEntity
												.getDatas().toArray(datas));
								XSSFDataValidation dataValidation = (XSSFDataValidation) validationHelper
										.createValidation(constraint, regions);
								// 将有效性验证添加到表单
								sheet.addValidationData(dataValidation);
							}
						}

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
}