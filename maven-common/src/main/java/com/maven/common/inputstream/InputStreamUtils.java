package com.maven.common.inputstream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 文件、文件流和Byte转换类
 * 
 * @author chenjian
 * @createDate 2019-09-12
 */
public class InputStreamUtils {

	private static Logger logger = LoggerFactory
			.getLogger(InputStreamUtils.class);

	/**
	 * File转InputStream(需要关闭流)
	 * 
	 * @param file
	 *            文件对象
	 * @return InputStream对象
	 */
	public static InputStream fileToInputStream(File file) {
		InputStream inputStream = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(file)) {
				inputStream = new FileInputStream(file);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return inputStream;
	}

	/**
	 * File转Byte
	 * 
	 * @param file
	 *            文件对象
	 * @return byte数组
	 */
	public static byte[] fileToByte(File file) {
		InputStream inputStream = null;
		byte[] byt = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(file)) {
				inputStream = new FileInputStream(file);
				byt = new byte[inputStream.available()];
				inputStream.read(byt);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return byt;
	}

	/**
	 * InputStream转File
	 * 
	 * @param inputStream
	 *            流对象
	 * @param filePath
	 *            文件路径
	 * @return File对象
	 */
	public static File inputStreamToFile(InputStream inputStream,
			String filePath) {
		OutputStream outputStream = null;
		File file = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(inputStream)
					&& StringUtils.isNotEmpty(filePath)) {
				file = new File(filePath);

				if (file.exists()) {
					file.createNewFile();
				}

				byte[] byt = new byte[inputStream.available()];
				inputStream.read(byt);

				outputStream = new FileOutputStream(file);
				outputStream.write(byt);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}

				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * InputStream转Byte
	 * 
	 * @param inputStream
	 *            流对象
	 * @return byte数组
	 */
	public static byte[] inputStreamToByte(InputStream inputStream) {
		byte[] byt = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(inputStream)) {
				byt = new byte[inputStream.available()];
				inputStream.read(byt);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(inputStream)) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return byt;
	}

	/**
	 * Byte转File
	 * 
	 * @param byt
	 *            Byte集合
	 * @param filePath
	 *            文件路径
	 * @return File对象
	 */
	public static File byteToFile(byte[] byt, String filePath) {
		OutputStream outputStream = null;
		File file = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(byt) && byt.length > 0
					&& StringUtils.isNotEmpty(filePath)) {
				file = new File(filePath);

				if (file.exists()) {
					file.createNewFile();
				}

				outputStream = new FileOutputStream(file);
				outputStream.write(byt);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(outputStream)) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * Byte转InputStream(需要关闭流)
	 * 
	 * @param byt
	 *            Byte集合
	 * @return InputStream对象
	 */
	public static InputStream byteToInputStream(byte[] byt) {
		InputStream inputStream = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(byt) && byt.length > 0) {
				inputStream = new ByteArrayInputStream(byt);

				message = "Change success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return inputStream;
	}
}