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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
	public static JSONArray read(String filePath) {
		JSONArray jsonArray = null;

		File file = new File(filePath);

		// 判断文件是否存在
		if (file.exists()) {
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
					.toLowerCase();

			// 判断文件后缀名
			switch (fileType) {
			case XLS_NAME:
				jsonArray = readXLS(filePath);
				break;
			case XLSX_NAME:
				jsonArray = readXLSX(filePath);
				break;
			default:
				logger.info("File format error");
				break;
			}
		} else {
			logger.info("File not exist");
		}

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
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 * @return
	 */
	public static boolean write(String filePath, JSONObject jsonObject) {
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
				writeXLS(filePath, jsonObject);
				break;
			default:
				writeXLSX(filePath, jsonObject);
				break;
			}

			success = true;
		} else {
			logger.info("File format error");
		}

		return success;
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param suffixIndex
	 *            文件后缀索引(1:xls文件,2:xlsx文件)
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 * @return
	 */
	public static byte[] write(int suffixIndex, JSONObject jsonObject) {
		byte[] byt = null;

		// 判断文件后缀索引
		switch (suffixIndex) {
		case XLS_INDEX:
			byt = writeXLS(jsonObject);
			break;
		case XLSX_INDEX:
			byt = writeXLSX(jsonObject);
			break;
		default:
			logger.info("File format error");
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
			logger.info("File format error");
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
			logger.info("File format error");
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
	 * @return
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
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 */
	private static void writeXLS(String filePath, JSONObject jsonObject) {
		FileOutputStream outputStream = null;
		HSSFWorkbook workbook = null;

		try {
			workbook = getHSSFWorkbook(jsonObject);

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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 */
	private static void writeXLSX(String filePath, JSONObject jsonObject) {
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = null;

		try {
			workbook = getXSSFWorkbook(jsonObject);

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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
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
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 * @return
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
						short fontSize = style.getShortValue("fontSize");
						short fontColor = style.getShortValue("fontColor");
						boolean bold = style.getBooleanValue("bold");
						boolean italic = style.getBooleanValue("italic");

						StyleEntity styleEntity = new StyleEntity(styleName);

						if (horizontal > 0) {
							styleEntity.setHorizontal(horizontal);
						}

						if (vertical > 0) {
							styleEntity.setVertical(vertical);
						}

						if (fontSize > 0) {
							styleEntity.setFontSize(fontSize);
						}

						if (fontColor > 0) {
							styleEntity.setFontColor(fontColor);
						}
						styleEntity.setBold(bold);
						styleEntity.setItalic(italic);

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
						JSONArray titles = sheet.getJSONArray("titles");
						JSONArray rows = sheet.getJSONArray("rows");

						SheetEntity sheetEntity = new SheetEntity(sheetName);

						// 判断是否有标题
						if (titles.size() > 0) {
							List<TitleEntity> titleList = new ArrayList<TitleEntity>();

							// 遍历标题
							for (int j = 0; j < titles.size(); j++) {
								JSONObject title = titles.getJSONObject(j);
								String titleName = title.getString("name");
								String styleName = title.getString("styleName");

								TitleEntity titleEntity = new TitleEntity(
										titleName);
								titleEntity.setStyleName(styleName);

								titleList.add(titleEntity);
							}

							sheetEntity.setTitles(titleList);
						}

						// 判断是否有行数据
						if (rows.size() > 0) {
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
								if (cells.size() > 0) {
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
	 * @param jsonObject
	 *            数据包含：1、'styles'-样式集合(JSONArray) 2、'sheets'-工作簿集合(JSONArray)
	 *            A、样式数据('name'-样式名称(String) 'horizontal'-水平位置(Integer)
	 *            'vertical'-垂直位置(Integer) 'fontSize'-字体大小(Short)
	 *            'fontColor'-字体颜色(Short) 'bold'-是否粗体(Boolean)
	 *            'italic'-是否斜体(Boolean)) B、工作簿数据('name'-工作簿名称(String)
	 *            'titles'-标题集合(JSONArray) 'rows'-行数据集合(JSONArray))
	 *            C、标题数据('name'-标题值(String) 'styleName'-样式名称(String))
	 *            D、行数据('height'-行高度(Short) 'cells'-单元格集合(JSONArray))
	 *            E、单元格数据('width'-单元格宽度(Integer) 'value'-单元格值(Object)
	 *            'styleName'-样式名称(String))
	 * @return
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
						short fontSize = style.getShortValue("fontSize");
						short fontColor = style.getShortValue("fontColor");
						boolean bold = style.getBooleanValue("bold");
						boolean italic = style.getBooleanValue("italic");

						StyleEntity styleEntity = new StyleEntity(styleName);
						styleEntity.setHorizontal(horizontal);
						styleEntity.setVertical(vertical);
						styleEntity.setFontSize(fontSize);
						styleEntity.setFontColor(fontColor);
						styleEntity.setBold(bold);
						styleEntity.setItalic(italic);

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
						JSONArray titles = sheet.getJSONArray("titles");
						JSONArray rows = sheet.getJSONArray("rows");

						SheetEntity sheetEntity = new SheetEntity(sheetName);

						// 判断是否有标题
						if (titles.size() > 0) {
							List<TitleEntity> titleList = new ArrayList<TitleEntity>();

							// 遍历标题
							for (int j = 0; j < titles.size(); j++) {
								JSONObject title = titles.getJSONObject(j);
								String titleName = title.getString("name");
								String styleName = title.getString("styleName");

								TitleEntity titleEntity = new TitleEntity(
										titleName);
								titleEntity.setStyleName(styleName);

								titleList.add(titleEntity);
							}

							sheetEntity.setTitles(titleList);
						}

						// 判断是否有行数据
						if (rows.size() > 0) {
							List<RowEntity> rowList = new ArrayList<RowEntity>();

							// 遍历行数据
							for (int j = 0; j < rows.size(); j++) {
								JSONObject row = rows.getJSONObject(j);
								short height = row.getShortValue("height");
								JSONArray cells = row.getJSONArray("cells");

								RowEntity rowEntity = new RowEntity();
								rowEntity.setHeight(height);

								// 判断是否单元格数据
								if (cells.size() > 0) {
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
}