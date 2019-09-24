package com.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.model.TextPieceTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Ts {

	public static JSONObject read(String filePath) {
		JSONObject jsonObject = new JSONObject();

		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(filePath));

			Element root = document.getRootElement();

			List<Element> list = root.elements();
			for (Element element : list) {
				if (element.elements().size() > 0) {
					List<Element> childList = element.elements();
					JSONArray childArray = new JSONArray();

					for (Element childElement : childList) {
						Element nameElement = childElement.element("name");
						Element sexElement = childElement.element("sex");
						Element ageElement = childElement.element("age");

						JSONObject childJSON = new JSONObject();
						childJSON.put(nameElement.getName(),
								nameElement.getText());
						childJSON.put(sexElement.getName(),
								sexElement.getText());
						childJSON.put(ageElement.getName(),
								Integer.valueOf(ageElement.getText()));

						childArray.add(childJSON);
					}

					jsonObject.put(element.getName(), childArray);
				} else {
					jsonObject.put(element.getName(), element.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("读取完毕!");

		return jsonObject;
	}

	public static void write(JSONObject jsonObject, String filePath) {
		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("root");

		Iterator<Entry<String, Object>> iterator = jsonObject.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object object = entry.getValue();

			Element element = root.addElement(key);

			if (object instanceof JSONArray) {
				JSONArray array = (JSONArray) object;

				for (Object obj : array) {
					Element child = element.addElement("person");

					JSONObject childJSON = (JSONObject) obj;
					Iterator<Entry<String, Object>> childIterator = childJSON
							.entrySet().iterator();
					while (childIterator.hasNext()) {
						Entry<String, Object> childEntry = childIterator.next();

						Element childInfo = child.addElement(childEntry
								.getKey());
						childInfo.setText(childEntry.getValue().toString());
					}
				}
			} else {
				element.setText(object.toString());
			}
		}

		try {
			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
			FileOutputStream fos = new FileOutputStream(filePath);
			writer.setOutputStream(fos);

			writer.write(document);
			System.out.println("写出完毕!");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		/**
		File file = new File("F:\\3.docx");
		FileInputStream inputStream = new FileInputStream(file); 
		
		XWPFDocument document = new XWPFDocument(inputStream);
		Iterator<IBodyElement> iterator = document.getBodyElementsIterator();
		while (iterator.hasNext()) {
			IBodyElement element = iterator.next();
			
			if (element instanceof XWPFParagraph) {
				XWPFParagraph paragraph = (XWPFParagraph) element;
				System.out.println(paragraph.getText());
			} else if (element instanceof XWPFTable) {
				XWPFTable table = (XWPFTable) element;
				
				for (int i = 0; i < table.getRows().size(); i++) {
					XWPFTableRow row = table.getRow(i);
					
					for (int j = 0; j < row.getTableCells().size(); j++) {
						XWPFTableCell cell = row.getCell(j);
						System.out.println(cell.getText());
					}
				}
			} else if (element instanceof XWPFPicture) {
				XWPFPicture picture = (XWPFPicture) element;
			}
		}
		
		List<XWPFPictureData> pictureList = document.getAllPictures();
		for (int i = 0; i < pictureList.size(); i++) {
			byte[] datas = pictureList.get(i).getData();
			FileOutputStream outputStream = new FileOutputStream("F:\\pic" + i + ".png");
			outputStream.write(datas);
		}
		**/
		
		File file = new File("F:\\2.doc");
		FileInputStream inputStream = new FileInputStream(file);
		
		HWPFDocument document = new HWPFDocument(inputStream);
		Range range = document.getRange();
		for (int i = 0; i < range.numParagraphs(); i++) {
			Paragraph paragraph = range.getParagraph(i);
			
			if (paragraph.isInList()) {
				//System.out.println(paragraph.text().replaceAll("", ""));
			}
		}
		
		TableIterator iterator = new TableIterator(range);
		while (iterator.hasNext()) {
			Table table = iterator.next();
			
			for (int i = 0; i < table.numRows(); i++) {
				TableRow row = table.getRow(i);
				
				for (int j = 0; j < row.numCells(); j++) {
					TableCell cell = row.getCell(j);
					
					for (int k = 0; k < cell.numParagraphs(); k++) {
						Paragraph paragraph = cell.getParagraph(k);
						System.out.println(paragraph.text().trim());
					}
				}
			}
		}
		
		byte[] dataStream = document.getDataStream();
		PicturesTable picturesTable = new PicturesTable(document, dataStream, dataStream);
		for (int i = 0; i < range.numCharacterRuns(); i++) {
			CharacterRun characterRun = range.getCharacterRun(i);
			boolean hasPic = picturesTable.hasPicture(characterRun);
			
			if (hasPic) {
				Picture picture = picturesTable.extractPicture(characterRun, true);
				//picture.writeImageContent(new FileOutputStream("F:\\pic" + i + ".png"));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				picture.writeImageContent(outputStream);
				System.out.println(outputStream.toByteArray().length);
			}
		}
		
		inputStream.close();
	}
}