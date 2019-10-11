package com.maven.common.picture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缩放图片类
 * 
 * @author chenjian
 * @createDate 2019-10-11
 */
public class PictureUtils {

	private static Logger logger = LoggerFactory.getLogger(PictureUtils.class);

	// 原始图片路径
	private static String sourcePath;
	// 目标路径
	private static String targetPath;
	// 宽度
	private static int width;
	// 高度
	private static int height;
	// 原始图片
	private static BufferedImage img;
	// 原始图片后缀名
	private static String suffix;

	/**
	 * 初始化图片
	 * 
	 * @param picPath
	 *            原始图片路径
	 * @param filePath
	 *            目标路径
	 * @param extName
	 *            扩展名
	 */
	private static void init(String picPath, String filePath, String extName) {
		try {
			sourcePath = picPath;

			// 判断是否有目标路径
			if (StringUtils.isNotEmpty(filePath)) {
				// 得到最后一个.的位置
				int index = filePath.lastIndexOf(".");
				// 获取被缩放的图片的格式
				suffix = filePath.substring(index + 1);
				targetPath = filePath;
			} else {
				// 得到最后一个.的位置
				int index = picPath.lastIndexOf(".");
				// 获取被缩放的图片的格式
				suffix = picPath.substring(index + 1);
				// 获取目标路径(和原始图片路径相同,在文件名后添加了一个_s)
				targetPath = picPath.substring(0, index) + "_" + extName + "."
						+ suffix;
			}

			create();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Init error");
		}
	}

	/**
	 * 创建原始图片Image对象
	 */
	private static void create() {
		try {
			// 读取图片,返回一个BufferedImage对象
			img = ImageIO.read(new File(sourcePath));
			// 获取图片的长和宽
			width = img.getWidth();
			height = img.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create error");
		}
	}

	/**
	 * 缩放图片
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	private static void zoomPic(int width, int height) {
		try {
			// 获取缩放后的Image对象
			Image _img = img.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			// 新建一个和Image对象相同大小的画布
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// 获取画笔
			Graphics2D graphics = image.createGraphics();
			// 将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
			graphics.drawImage(_img, 0, 0, null);
			// 释放资源
			graphics.dispose();
			// 使用ImageIO的方法进行输出,记得关闭资源
			OutputStream out = new FileOutputStream(targetPath);
			ImageIO.write(image, suffix, out);
			out.close();

			logger.info("Zoom success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Zoom error");
		}
	}

	/**
	 * 按比例缩放图片
	 * 
	 * @param picPath
	 *            原始图片路径
	 * @param extName
	 *            扩展名
	 * @param scale
	 *            缩放比例
	 */
	public static boolean zoomByScale(String picPath, String extName,
			double scale) {
		boolean result = true;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(extName)
				&& scale > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				init(picPath, "", extName);

				// 获取缩放后的长和宽
				int _width = (int) (scale * width);
				int _height = (int) (scale * height);

				zoomPic(_width, _height);
				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按高宽缩放图片
	 * 
	 * @param picPath
	 *            原始图片路径
	 * @param extName
	 *            扩展名
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public static boolean zoomBySize(String picPath, String extName, int width,
			int height) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(extName)
				&& width > 0 && height > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				init(picPath, "", extName);
				zoomPic(width, height);
				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按比例缩放图片
	 * 
	 * @param picPath
	 *            原始图片路径
	 * @param filePath
	 *            目标路径
	 * @param fileName
	 *            目标文件名
	 * @param scale
	 *            缩放比例
	 * @return
	 */
	public static boolean zoomToTarget(String picPath, String filePath,
			String fileName, double scale) {
		boolean result = true;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(fileName) && scale > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				file = new File(filePath);

				// 判断目标文件路径是否存在
				if (!file.exists()) {
					file.mkdirs();
				}

				init(picPath, filePath + File.separator + fileName, "");

				// 获取缩放后的长和宽
				int _width = (int) (scale * width);
				int _height = (int) (scale * height);

				zoomPic(_width, _height);
				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按高宽缩放图片
	 * 
	 * @param picPath
	 *            始图片路径
	 * @param filePath
	 *            目标路径
	 * @param fileName
	 *            目标文件名
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 */
	public static boolean zoomToTarget(String picPath, String filePath,
			String fileName, int width, int height) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(fileName) && width > 0 && height > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				file = new File(filePath);

				// 判断目标文件路径是否存在
				if (!file.exists()) {
					file.mkdirs();
				}

				init(picPath, filePath + File.separator + fileName, "");
				zoomPic(width, height);
				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}
}