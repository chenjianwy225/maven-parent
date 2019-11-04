package com.maven.common.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;

/**
 * 读写DOC类
 * 
 * @author chenjian
 * @createDate 2019-09-23
 */
public class DOCUtils {

	private static Logger logger = LoggerFactory.getLogger(DOCUtils.class);

	// DOC文件后缀名
	private static final String DOC_NAME = "doc";

	// DOCX文件后缀名
	private static final String DOCX_NAME = "docx";

	// 段落类型
	public static final int PARAGRAPH = 1;

	// 表格类型
	public static final int TABLE = 2;

	// 图片类型
	public static final int PICTURE = 3;

	// 默认表格宽度
	private static final int DEFAULT_TABLE_WIDTH = 1500;

	// 默认表格高度
	private static final int DEFAULT_TABLE_HEIGHT = 500;

	// 默认图片宽度
	private static final int DEFAULT_PICTURE_WIDTH = 400;

	// 默认图片高度
	private static final int DEFAULT_PICTURE_HEIGHT = 300;

	/**
	 * 读文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONObject数据
	 */
	public static JSONObject read(String filePath) {
		JSONObject jsonObject = null;
		String message = "Parameter error";

		// 判断传入参数
		if (StringUtils.isNotEmpty(filePath)) {
			message = "File not exist";

			File file = new File(filePath);

			// 判断文件是否存在
			if (file.exists()) {
				String fileType = filePath.substring(
						filePath.lastIndexOf(".") + 1).toLowerCase();

				message = "Read DOC/DOCX file fail";
				// 判断文件后缀名
				switch (fileType) {
				case DOC_NAME:
					jsonObject = readDOC(filePath);
					break;
				case DOCX_NAME:
					jsonObject = readDOCX(filePath);
					break;
				default:
					message = "File format error";
					break;
				}

				// 判断是否读取成功
				if (StringUtils.isNotEmpty(jsonObject)) {
					message = "Read DOC file success";
				}
			}
		}

		logger.info(message);

		return jsonObject;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径 * @param jsonArray 数据集合: 1、'type':1-段落、2-表格、3-图片
	 *            2、'value':数据(段落为String、表格为List<List<object>>、图片为byte[])
	 *            3、'width':图片宽度(只用于图片,不设默认400px)
	 *            4、'height':图片高度(只用于图片,不设默认300px)
	 * @return 是否写入成功
	 */
	public static boolean write(String filePath, JSONArray jsonArray) {
		boolean result = false;
		OutputStream outputStream = null;
		XWPFDocument document = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(filePath)
					&& StringUtils.isNotEmpty(jsonArray)) {
				message = "File format error";

				String fileType = filePath.substring(
						filePath.lastIndexOf(".") + 1).toLowerCase();

				// 判断文件后缀名
				if (fileType.equalsIgnoreCase(DOC_NAME)
						|| fileType.equalsIgnoreCase(DOCX_NAME)) {
					message = "Write DOC/DOCX file fail";

					String dir = filePath.substring(0,
							filePath.lastIndexOf("\\"));

					// 判断文件夹是否存在
					File file = new File(dir);
					if (!file.exists()) {
						file.mkdirs();
					}

					document = getXWPFDocument(jsonArray);

					// 判断XWPFDocument
					if (StringUtils.isNotEmpty(document)) {
						outputStream = new FileOutputStream(new File(filePath));
						document.write(outputStream);

						message = "Write DOC/DOCX file success";
					}
				}
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write DOC/DOCX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}

				if (StringUtils.isNotEmpty(document)) {
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param filePath
	 *            文件路径 * @param jsonArray 数据集合: 1、'type':1-段落、2-表格、3-图片
	 *            2、'value':数据(段落为String、表格为List<List<object>>、图片为byte[])
	 *            3、'width':图片宽度(只用于图片,不设默认400px)
	 *            4、'height':图片高度(只用于图片,不设默认300px)
	 * @return byte数组
	 */
	public static byte[] write(JSONArray jsonArray) {
		ByteArrayOutputStream outputStream = null;
		XWPFDocument document = null;
		byte[] byt = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(jsonArray)) {
				message = "Write DOC/DOCX file fail";

				document = getXWPFDocument(jsonArray);

				// 判断XWPFDocument
				if (StringUtils.isNotEmpty(document)) {
					outputStream = new ByteArrayOutputStream();
					document.write(outputStream);
					byt = outputStream.toByteArray();

					message = "Write DOC/DOCX file success";
				}
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write DOC/DOCX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}

				if (StringUtils.isNotEmpty(document)) {
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return byt;
	}

	/**
	 * 获取XWPFDocument
	 * 
	 * @param jsonArray
	 *            数据集合: 1、'type':1-段落、2-表格、3-图片 2、'value':数据(
	 *            段落为String、表格为List<List<object>>、图片为byte[] ）
	 *            3、'width':图片宽度(只用于图片,不设默认400px)
	 *            4、'height':图片高度(只用于图片,不设默认300px)
	 * @return XWPFDocument对象
	 */
	private static XWPFDocument getXWPFDocument(JSONArray jsonArray) {
		XWPFDocument document = null;

		try {
			document = new XWPFDocument();

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				int type = jsonObject.getIntValue("type");
				int width = 0, height = 0;

				switch (type) {
				case PARAGRAPH:
					String value = jsonObject.getString("value");
					XWPFParagraph paragraph = document.createParagraph();
					XWPFRun run = paragraph.createRun();

					run.setBold(true);
					run.setFontSize(18);
					run.setText(value);
					break;
				case TABLE:
					JSONArray array = jsonObject.getJSONArray("value");
					width = StringUtils.isNotEmpty(jsonObject.get("width")) ? jsonObject
							.getIntValue("width") : DEFAULT_TABLE_WIDTH;
					height = StringUtils.isNotEmpty(jsonObject.get("height")) ? jsonObject
							.getIntValue("height") : DEFAULT_TABLE_HEIGHT;

					if (array.size() > 0) {
						int rows = array.size();
						int cols = array.getJSONArray(0).size();
						XWPFTable table = document.createTable(rows, cols);

						for (int j = 0; j < rows; j++) {
							JSONArray datas = array.getJSONArray(j);
							XWPFTableRow row = table.getRow(j);
							row.setHeight(height);

							for (int k = 0; k < cols; k++) {
								XWPFTableCell cell = row.getCell(k);
								cell.setVerticalAlignment(XWPFVertAlign.CENTER);
								cell.setWidth(Integer.valueOf(width).toString());
								cell.setText(datas.get(k).toString());
								CTTc cttc = cell.getCTTc();
								CTTcPr ctPr = cttc.addNewTcPr();
								ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
								cttc.getPList().get(0).addNewPPr().addNewJc()
										.setVal(STJc.CENTER);
							}
						}

						XWPFParagraph tableParagraph = document
								.createParagraph();
						XWPFRun tableRun = tableParagraph.createRun();
						tableRun.addBreak();
					}
					break;
				case PICTURE:
					byte[] byt = jsonObject.getBytes("value");
					width = StringUtils.isNotEmpty(jsonObject.get("width")) ? jsonObject
							.getIntValue("width") : DEFAULT_PICTURE_WIDTH;
					height = StringUtils.isNotEmpty(jsonObject.get("height")) ? jsonObject
							.getIntValue("height") : DEFAULT_PICTURE_HEIGHT;

					XWPFParagraph p = document.createParagraph();
					String blipId = document.addPictureData(byt,
							XWPFDocument.PICTURE_TYPE_PNG);
					CustomXWPFDocument.createPicture(blipId, document
							.getAllPictures().size() - 1, width, height, p);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write DOC/DOCX file error");
		}

		return document;
	}

	/**
	 * 读DOC文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONObject对象
	 */
	private static JSONObject readDOC(String filePath) {
		JSONObject jsonObject = null;
		InputStream inputStream = null;
		HWPFDocument document = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			document = new HWPFDocument(inputStream);
			Range range = document.getRange();

			jsonObject = new JSONObject();

			// 获取所有段落
			List<String> paragraphList = new ArrayList<String>();
			for (int i = 0; i < range.numParagraphs(); i++) {
				Paragraph paragraph = range.getParagraph(i);
				String content = paragraph.text().trim().replaceAll("", "");

				if (!paragraph.isInTable() && StringUtils.isNotEmpty(content)) {
					paragraphList.add(content);
				}
			}
			jsonObject.put("paragraphs", paragraphList);

			// 获取所有表格
			List<List<Object>> tableList = new ArrayList<List<Object>>();
			TableIterator iterator = new TableIterator(range);
			while (iterator.hasNext()) {
				Table table = iterator.next();
				List<Object> list = new ArrayList<Object>();

				for (int i = 0; i < table.numRows(); i++) {
					TableRow row = table.getRow(i);

					for (int j = 0; j < row.numCells(); j++) {
						TableCell cell = row.getCell(j);
						list.add(cell.text().trim().replaceAll("", ""));
					}
				}

				tableList.add(list);
			}
			jsonObject.put("tables", tableList);

			// 获取所有图片
			List<byte[]> pictureList = new ArrayList<byte[]>();
			byte[] dataStream = document.getDataStream();
			PicturesTable picturesTable = new PicturesTable(document,
					dataStream, dataStream);
			for (int i = 0; i < range.numCharacterRuns(); i++) {
				CharacterRun characterRun = range.getCharacterRun(i);
				boolean hasPic = picturesTable.hasPicture(characterRun);

				if (hasPic) {
					Picture picture = picturesTable.extractPicture(
							characterRun, true);
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					picture.writeImageContent(outputStream);
					pictureList.add(outputStream.toByteArray());

					outputStream.close();
				}
			}
			jsonObject.put("pictures", pictureList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read DOC file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}

				if (StringUtils.isNotEmpty(document)) {
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;
	}

	/**
	 * 读DOCX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONObject对象
	 */
	private static JSONObject readDOCX(String filePath) {
		JSONObject jsonObject = null;
		InputStream inputStream = null;
		XWPFDocument document = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			document = new XWPFDocument(inputStream);

			jsonObject = new JSONObject();

			// 获取所有段落
			List<String> paragraphList = new ArrayList<String>();
			List<XWPFParagraph> list_paragraph = document.getParagraphs();
			for (XWPFParagraph paragraph : list_paragraph) {
				String content = paragraph.getText();

				if (StringUtils.isNotEmpty(content)) {
					paragraphList.add(content);
				}
			}
			jsonObject.put("paragraphs", paragraphList);

			// 获取所有表格
			List<List<Object>> tableList = new ArrayList<List<Object>>();
			List<XWPFTable> list_table = document.getTables();
			for (XWPFTable talbe : list_table) {
				List<XWPFTableRow> rows = talbe.getRows();
				List<Object> list = new ArrayList<Object>();

				for (XWPFTableRow row : rows) {
					List<XWPFTableCell> cells = row.getTableCells();

					for (XWPFTableCell cell : cells) {
						list.add(cell.getText());
					}
				}

				tableList.add(list);
			}
			jsonObject.put("tables", tableList);

			// 获取所有图片
			List<byte[]> pictureList = new ArrayList<byte[]>();
			List<XWPFPictureData> ist_picture = document.getAllPictures();
			for (XWPFPictureData picture : ist_picture) {
				pictureList.add(picture.getData());
			}
			jsonObject.put("pictures", pictureList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read DOCX file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}

				if (StringUtils.isNotEmpty(document)) {
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;
	}
}