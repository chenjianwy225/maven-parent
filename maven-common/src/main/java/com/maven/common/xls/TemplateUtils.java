package com.maven.common.xls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 模板文件生成类
 * 
 * @author chenjian
 * @createDate 2019-09-17
 */
public class TemplateUtils {

	private static Logger logger = LoggerFactory.getLogger(TemplateUtils.class);

	// XLS文件后缀名
	private static final String XLS_NAME = "xls";

	/**
	 * 通过模板生成文件(File)
	 * 
	 * @param templatePath
	 *            模板文件路径
	 * @param templateName
	 *            模板文件名
	 * @param excelPath
	 *            Excel文件路径
	 * @param datas
	 *            数据集合
	 */
	@SuppressWarnings("deprecation")
	public static void create(String templatePath, String templateName,
			String excelPath, Map<String, Object> datas) {
		Writer writer = null;

		try {
			File file = new File(templatePath);

			// 判断模板文件是否存在
			if (file.exists()) {
				String fileType = excelPath.substring(
						excelPath.lastIndexOf(".") + 1).toLowerCase();

				// 判断文件后缀名
				if (fileType.equalsIgnoreCase(XLS_NAME)) {
					String dir = excelPath.substring(0,
							excelPath.lastIndexOf("\\"));

					file = new File(dir);
					if (!file.exists()) {
						file.mkdirs();
					}

					Configuration configuration = new Configuration();
					configuration.setDefaultEncoding("UTF-8");
					configuration.setDirectoryForTemplateLoading(new File(
							templatePath));
					configuration.setObjectWrapper(new DefaultObjectWrapper());

					Template template = configuration.getTemplate(templateName,
							"UTF-8");
					writer = new OutputStreamWriter(new FileOutputStream(
							excelPath), "UTF-8");
					template.process(datas, writer);
					writer.flush();
				} else {
					logger.info("File format error");
				}
			} else {
				logger.info("Template file not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create XLS file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(writer)) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过模板生成文件(Byte)
	 * 
	 * @param templatePath
	 *            模板文件路径
	 * @param templateName
	 *            模板文件名
	 * @param datas
	 *            数据集合
	 */
	@SuppressWarnings("deprecation")
	public static byte[] create(String templatePath, String templateName,
			Map<String, Object> datas) {
		Writer writer = null;
		byte[] byt = null;

		try {
			File file = new File(templatePath);

			// 判断模板文件是否存在
			if (file.exists()) {
				Configuration configuration = new Configuration();
				configuration.setDefaultEncoding("UTF-8");
				configuration.setDirectoryForTemplateLoading(new File(
						templatePath));
				configuration.setObjectWrapper(new DefaultObjectWrapper());

				Template template = configuration.getTemplate(templateName,
						"UTF-8");
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				writer = new OutputStreamWriter(outputStream, "UTF-8");
				template.process(datas, writer);
				byt = outputStream.toByteArray();
			} else {
				logger.info("Template file not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create XLS file error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(writer)) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return byt;
	}
}