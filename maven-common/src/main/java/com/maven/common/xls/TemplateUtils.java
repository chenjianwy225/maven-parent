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
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("UTF-8");
			configuration
					.setDirectoryForTemplateLoading(new File(templatePath));
			configuration.setObjectWrapper(new DefaultObjectWrapper());

			Template template = configuration
					.getTemplate(templateName, "UTF-8");
			writer = new OutputStreamWriter(new FileOutputStream(excelPath),
					"UTF-8");
			template.process(datas, writer);
			writer.flush();
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
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("UTF-8");
			configuration
					.setDirectoryForTemplateLoading(new File(templatePath));
			configuration.setObjectWrapper(new DefaultObjectWrapper());

			Template template = configuration
					.getTemplate(templateName, "UTF-8");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			writer = new OutputStreamWriter(outputStream, "UTF-8");
			template.process(datas, writer);
			byt = outputStream.toByteArray();
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