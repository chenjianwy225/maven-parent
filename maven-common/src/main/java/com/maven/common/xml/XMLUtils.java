package com.maven.common.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;

/**
 * 读写XML类
 * 
 * @author chenjian
 * @createDate 2019-09-25
 */
public class XMLUtils {

	private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

	// XML文件后缀名
	private static final String XML_NAME = "xml";

	// XML文件编码
	private static final String XML_ENCODER = "UTF-8";

	/**
	 * 读文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return JSONObject对象
	 */
	public static JSONObject read(String filePath) {
		JSONObject jsonObject = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(filePath)) {
				message = "File not exist";

				File file = new File(filePath);

				// 判断文件是否存在
				if (file.exists()) {
					String fileType = filePath.substring(
							filePath.lastIndexOf(".") + 1).toLowerCase();

					// 判断文件后缀名
					if (fileType.equalsIgnoreCase(XML_NAME)) {
						SAXReader reader = new SAXReader();
						Document document = reader.read(new File(filePath));

						Element root = document.getRootElement();
						jsonObject = structureData(root);

						message = "Read XML file success";
					} else {
						message = "File format error";
					}
				}
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read XML file error");
		}

		return jsonObject;
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param jsonObject
	 *            JSON数据对象(1、'attributes'-属性集合 2、'lists'-子元素集合 3、'values'-元素值集合)
	 */
	public static void write(String filePath, JSONObject jsonObject) {
		Writer out = null;
		XMLWriter writer = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(filePath)
					&& StringUtils.isNotEmpty(jsonObject)) {
				message = "File format error";

				String fileType = filePath.substring(
						filePath.lastIndexOf(".") + 1).toLowerCase();

				// 判断文件后缀名
				if (fileType.equalsIgnoreCase(XML_NAME)) {
					String dir = filePath.substring(0,
							filePath.lastIndexOf("\\"));

					// 判断文件夹是否存在
					File file = new File(dir);
					if (!file.exists()) {
						file.mkdirs();
					}

					Document document = DocumentHelper.createDocument();
					structureXML(document, jsonObject);

					// 创建输出流
					out = new PrintWriter(filePath, XML_ENCODER);
					// 格式化
					OutputFormat format = new OutputFormat("\t", true);
					// 去掉原来的空白(\t和换行和空格)
					format.setTrimText(true);

					writer = new XMLWriter(out, format);
					writer.write(document);

					message = "Write XML file success";
				}
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XML file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(out)) {
					out.close();
				}

				if (StringUtils.isNotEmpty(writer)) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写文件(Byte)
	 * 
	 * @param jsonObject
	 *            JSON数据对象(1、'attributes'-属性集合 2、'lists'-子元素集合 3、'values'-元素值集合)
	 * @return byte数组
	 */
	public static byte[] write(JSONObject jsonObject) {
		ByteArrayOutputStream out = null;
		XMLWriter writer = null;
		byte[] byt = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(jsonObject)) {

				Document document = DocumentHelper.createDocument();
				structureXML(document, jsonObject);

				// 创建输出流
				out = new ByteArrayOutputStream();
				// 格式化
				OutputFormat format = new OutputFormat("\t", true);
				// 去掉原来的空白(\t和换行和空格)
				format.setTrimText(true);

				writer = new XMLWriter(out, format);
				writer.write(document);
				byt = out.toByteArray();

				message = "Write XML file success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Write XML file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(out)) {
					out.close();
				}

				if (StringUtils.isNotEmpty(writer)) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return byt;
	}

	/**
	 * 遍历元素(针对XML读文件)
	 * 
	 * @param element
	 *            元素对象
	 * @return JSONObject对象
	 */
	private static JSONObject structureData(Element element) {
		JSONObject json = new JSONObject();

		String key = element.getName();
		List<Attribute> attributes = element.attributes();
		List<Element> elements = element.elements();

		JSONObject object = new JSONObject();

		// 遍历元素属性
		if (attributes.size() > 0) {
			JSONObject attributeJson = new JSONObject();

			for (Attribute attribute : attributes) {
				attributeJson.put(attribute.getName(), attribute.getValue());
			}

			object.put("attributes", attributeJson);
		}

		// 遍历元素子元素
		if (elements.size() > 0) {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();

			for (Element childElement : elements) {
				List<Element> ls = childElement.elements();

				if (ls.size() > 0) {
					jsonArray.add(structureData(childElement));
				} else {
					jsonObject.put(childElement.getName(),
							childElement.getStringValue());
				}
			}

			if (jsonArray.size() > 0) {
				object.put("lists", jsonArray);
			}

			if (jsonObject.size() > 0) {
				object.put("values", jsonObject);
			}
		}

		json.put(key, object);
		return json;
	}

	/**
	 * 构建对象(针对XML写文件)
	 * 
	 * @param object
	 *            XML对象
	 * @param json
	 *            JSON数据对象(1、'attributes'-属性集合 2、'lists'-子元素集合 3、'values'-元素值集合)
	 */
	private static void structureXML(Object object, JSONObject json) {
		// 判断元素是Document或Element
		if (object instanceof Document) {
			Document document = (Document) object;

			Iterator<Entry<String, Object>> iterator = json.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				JSONObject jsonObject = JSONObject.parseObject(entry.getValue()
						.toString());

				Element element = document.addElement(entry.getKey());
				structureXML(element, jsonObject);
			}
		} else {
			Element element = (Element) object;

			JSONObject attributes = json.getJSONObject("attributes");
			JSONArray lists = json.getJSONArray("lists");
			JSONObject values = json.getJSONObject("values");

			// 判断元素是否有属性
			if (StringUtils.isNotEmpty(attributes) && attributes.size() > 0) {
				Iterator<Entry<String, Object>> attributeIterator = attributes
						.entrySet().iterator();

				while (attributeIterator.hasNext()) {
					Entry<String, Object> attributeEntry = attributeIterator
							.next();

					element.addAttribute(attributeEntry.getKey(),
							attributeEntry.getValue().toString());
				}
			}

			// 判断元素是否有子元素(有下一级子元素)
			if (StringUtils.isNotEmpty(lists) && lists.size() > 0) {
				for (int i = 0; i < lists.size(); i++) {
					JSONObject childJSON = JSONObject.parseObject(lists
							.getString(i));
					Iterator<Entry<String, Object>> childIterator = childJSON
							.entrySet().iterator();

					while (childIterator.hasNext()) {
						Entry<String, Object> entry = childIterator.next();
						JSONObject jsonObject = JSONObject.parseObject(entry
								.getValue().toString());

						Element childElement = element.addElement(entry
								.getKey());
						structureXML(childElement, jsonObject);
					}
				}
			}

			// 判断元素是否有子元素(无下一级子元素)
			if (StringUtils.isNotEmpty(values) && values.size() > 0) {
				Iterator<Entry<String, Object>> valueIterator = values
						.entrySet().iterator();

				while (valueIterator.hasNext()) {
					Entry<String, Object> valueEntry = valueIterator.next();

					element.addElement(valueEntry.getKey()).addText(
							valueEntry.getValue().toString());
				}
			}
		}
	}
}