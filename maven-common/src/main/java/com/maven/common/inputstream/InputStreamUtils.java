package com.maven.common.inputstream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.maven.common.StringUtils;

/**
 * 文件、文件流和Byte转换类
 * 
 * @author chenjian
 * @createDate 2019-09-12
 */
public class InputStreamUtils {

	/**
	 * File转InputStream(需要关闭流)
	 * 
	 * @param file
	 *            文件对象
	 * @return
	 */
	public static InputStream fileToInputStream(File file) {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * File转Byte
	 * 
	 * @param file
	 *            文件对象
	 * @return
	 */
	public static byte[] fileToByte(File file) {
		InputStream inputStream = null;
		byte[] byt = null;

		try {
			inputStream = new FileInputStream(file);
			byt = new byte[inputStream.available()];
			inputStream.read(byt);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return
	 */
	public static File inputStreamToFile(InputStream inputStream,
			String filePath) {
		OutputStream outputStream = null;
		File file = null;

		try {
			file = new File(filePath);

			if (file.exists()) {
				file.createNewFile();
			}

			byte[] byt = new byte[inputStream.available()];
			inputStream.read(byt);

			outputStream = new FileOutputStream(file);
			outputStream.write(byt);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return
	 */
	public static byte[] inputStreamToByte(InputStream inputStream) {
		byte[] byt = null;

		try {
			byt = new byte[inputStream.available()];
			inputStream.read(byt);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return
	 */
	public static File byteToFile(byte[] byt, String filePath) {
		OutputStream outputStream = null;
		File file = null;

		try {
			file = new File(filePath);

			if (file.exists()) {
				file.createNewFile();
			}

			outputStream = new FileOutputStream(file);
			outputStream.write(byt);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return
	 */
	public static InputStream byteToInputStream(byte[] byt) {
		InputStream inputStream = null;

		try {
			inputStream = new ByteArrayInputStream(byt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputStream;
	}
}