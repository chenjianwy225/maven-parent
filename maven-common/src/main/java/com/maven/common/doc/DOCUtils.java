package com.maven.common.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;
import com.maven.common.request.MapUtils;

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

	/**
	 * 读文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static Map<String, Object> read(String filePath) {
		Map<String, Object> map = null;
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toLowerCase();

		// 判断文件后缀名
		switch (fileType) {
		case DOC_NAME:
			map = readDOC(filePath);
			break;
		case DOCX_NAME:
			map = readDOCX(filePath);
			break;
		default:
			logger.info("File format error");
			break;
		}

		return map;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param list
	 *            数据集合: 1、type:1-段落、2-表格、3-图片 2、value:数据(
	 *            段落为String、表格为List<List<object>>、图片为byte[] ）
	 *            3、width:图片宽度(只用于图片,不设默认400px) 4、height:图片高度(只用于图片,不设默认300px)
	 */
	public static void write(String filePath, List<Map<String, Object>> list) {
		OutputStream outputStream = null;
		XWPFDocument document = null;

		try {
			String fileType = filePath.substring(filePath.lastIndexOf(".") + 1)
					.toLowerCase();

			// 判断文件后缀名
			if (fileType.equalsIgnoreCase(DOC_NAME)
					|| fileType.equalsIgnoreCase(DOCX_NAME)) {
				String dir = filePath.substring(0, filePath.lastIndexOf("\\"));

				File file = new File(dir);
				if (!file.exists()) {
					file.mkdirs();
				}

				document = getXWPFDocument(list);

				// 判断XWPFDocument
				if (StringUtils.isNotEmpty(document)) {
					outputStream = new FileOutputStream(new File(filePath));
					document.write(outputStream);
				}
			} else {
				logger.info("File format error");
			}
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
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param list
	 *            数据集合: 1、type:1-段落、2-表格、3-图片 2、value:数据(
	 *            段落为String、表格为List<List<object>>、图片为byte[] ）
	 *            3、width:图片宽度(只用于图片,不设默认400px) 4、height:图片高度(只用于图片,不设默认300px)
	 */
	public static byte[] write(List<Map<String, Object>> list) {
		ByteArrayOutputStream outputStream = null;
		XWPFDocument document = null;
		byte[] byt = null;

		try {
			document = getXWPFDocument(list);

			// 判断XWPFDocument
			if (StringUtils.isNotEmpty(document)) {
				outputStream = new ByteArrayOutputStream();
				document.write(outputStream);
				byt = outputStream.toByteArray();
			}
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
	 * @param list
	 *            数据集合: 1、type:1-段落、2-表格、3-图片 2、value:数据(
	 *            段落为String、表格为List<List<object>>、图片为byte[] ）
	 *            3、width:图片宽度(只用于图片,不设默认400px) 4、height:图片高度(只用于图片,不设默认300px)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static XWPFDocument getXWPFDocument(List<Map<String, Object>> list) {
		XWPFDocument document = null;

		try {
			document = new XWPFDocument();

			for (Map<String, Object> map : list) {
				int type = MapUtils.getInteger(map, "type").intValue();
				Object value = MapUtils.get(map, "value");

				switch (type) {
				case PARAGRAPH:
					XWPFParagraph paragraph = document.createParagraph();
					XWPFRun run = paragraph.createRun();

					run.setBold(true);
					run.setFontSize(18);
					run.setText(value.toString());
					break;
				case TABLE:
					List<List<Object>> tableList = (List<List<Object>>) value;

					if (tableList.size() > 0) {
						int rows = tableList.size();
						int cols = tableList.get(0).size();
						XWPFTable table = document.createTable(rows, cols);

						for (int i = 0; i < rows; i++) {
							List<Object> rowList = tableList.get(i);
							XWPFTableRow row = table.getRow(i);

							for (int j = 0; j < cols; j++) {
								row.getCell(j).setVerticalAlignment(
										XWPFVertAlign.CENTER);
								row.getCell(j).setWidth("1000");
								row.getCell(j).setText(
										rowList.get(j).toString());
							}
						}

						XWPFParagraph tableParagraph = document
								.createParagraph();
						XWPFRun tableRun = tableParagraph.createRun();
						tableRun.addBreak();
					}
					break;
				case PICTURE:
					byte[] bs = (byte[]) value;
					int width = MapUtils.getInteger(map, "width", 400);
					int height = MapUtils.getInteger(map, "height", 300);

					XWPFParagraph p = document.createParagraph();
					String blipId = document.addPictureData(bs,
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
	 * @return
	 */
	private static Map<String, Object> readDOC(String filePath) {
		Map<String, Object> map = null;
		InputStream inputStream = null;
		HWPFDocument document = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			document = new HWPFDocument(inputStream);
			Range range = document.getRange();

			map = new HashMap<String, Object>();

			// 获取所有段落
			List<String> paragraphList = new ArrayList<String>();
			for (int i = 0; i < range.numParagraphs(); i++) {
				Paragraph paragraph = range.getParagraph(i);
				String content = paragraph.text().trim().replaceAll("", "");

				if (!paragraph.isInTable() && StringUtils.isNotEmpty(content)) {
					paragraphList.add(content);
				}
			}
			map.put("paragraphs", paragraphList);

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
			map.put("tables", tableList);

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
			map.put("pictures", pictureList);
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

		return map;
	}

	/**
	 * 读DOCX文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	private static Map<String, Object> readDOCX(String filePath) {
		Map<String, Object> map = null;
		InputStream inputStream = null;
		XWPFDocument document = null;

		try {
			inputStream = new FileInputStream(new File(filePath));
			document = new XWPFDocument(inputStream);

			map = new HashMap<String, Object>();

			// 获取所有段落
			List<String> paragraphList = new ArrayList<String>();
			List<XWPFParagraph> list_paragraph = document.getParagraphs();
			for (XWPFParagraph paragraph : list_paragraph) {
				String content = paragraph.getText();

				if (StringUtils.isNotEmpty(content)) {
					paragraphList.add(content);
				}
			}
			map.put("paragraphs", paragraphList);

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
			map.put("tables", tableList);

			// 获取所有图片
			List<byte[]> pictureList = new ArrayList<byte[]>();
			List<XWPFPictureData> ist_picture = document.getAllPictures();
			for (XWPFPictureData picture : ist_picture) {
				pictureList.add(picture.getData());
			}
			map.put("pictures", pictureList);
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

		return map;
	}
}