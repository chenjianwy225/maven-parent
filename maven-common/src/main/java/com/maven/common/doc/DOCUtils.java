package com.maven.common.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.maven.common.StringUtils;

/**
 * 读写DOC类
 * 
 * @author chenjian
 * @createDate 2019-09-23
 */
public class DOCUtils {

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

		if (fileType.equalsIgnoreCase("doc")) {
			map = readDOC(filePath);
		} else if (fileType.equalsIgnoreCase("docx")) {
			map = readDOCX(filePath);
		}

		return map;
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

	public static void main(String[] args) {
		String filePath = "F:\\3.docx";
		Map<String, Object> map = read(filePath);
		System.out.println(map.size());
	}
}