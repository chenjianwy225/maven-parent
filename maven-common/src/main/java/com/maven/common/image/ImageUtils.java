package com.maven.common.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 处理图片类
 * 
 * @author chenjian
 * @createDate 2019-10-11
 */
public class ImageUtils {

	private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

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
	private static String sourceSuffix;

	// 目标图片后缀名
	private static String targetSuffix = "png";

	// 左上
	public static final int TOP_LEFT = 1;

	// 中上
	public static final int TOP = 2;

	// 右上
	public static final int TOP_RIGHT = 3;

	// 左中
	public static final int CENTER_LEFT = 4;

	// 正中
	public static final int CENTER = 5;

	// 右中
	public static final int CENTER_RIGHT = 6;

	// 左下
	public static final int BOTTOM_LEFT = 7;

	// 中下
	public static final int BOTTOM = 8;

	// 右下
	public static final int BOTTOM_RIGHT = 9;

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

			// 得到最后一个.的位置
			int index = picPath.lastIndexOf(".");
			// 获取被缩放的图片的格式
			sourceSuffix = picPath.substring(index + 1).toLowerCase();

			// 判断是否有目标路径
			if (StringUtils.isNotEmpty(filePath)) {
				// 得到最后一个.的位置
				index = filePath.lastIndexOf(".");

				if (index != -1) {
					// 获取被缩放的图片的格式
					String fileType = filePath.substring(index + 1);
					if (StringUtils.isNotEmpty(fileType)) {
						targetSuffix = fileType.toLowerCase();
						targetPath = filePath;
					} else {
						targetPath = filePath + targetSuffix;
					}
				} else {
					targetPath = filePath + "." + targetSuffix;
				}
			} else {
				targetSuffix = sourceSuffix;
				// 获取目标路径(和原始图片路径相同,在文件名后添加了一个_s)
				targetPath = picPath.substring(0, index) + "_" + extName + "."
						+ sourceSuffix;
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
	 * 计算截取截取图片的信息
	 * 
	 * @param position
	 *            位置索引
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	private static Map<String, Integer> cutImageInfo(int position, int w, int h) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int x = 0, y = 0;

		// 判断截取宽度和高度是是否超出原始图片
		w = w > width ? width : w;
		h = h > height ? height : h;

		// 判断位置索引
		if (position == TOP_LEFT || position == TOP || position == TOP_RIGHT) {
			switch (position) {
			case TOP_LEFT:
				x = 0;
				break;
			case TOP:
				x = (width - w) / 2;
				break;
			default:
				x = width - w;
				break;
			}
			y = 0;
		} else if (position == CENTER_LEFT || position == CENTER
				|| position == CENTER_RIGHT) {
			switch (position) {
			case CENTER_LEFT:
				x = 0;
				break;
			case CENTER:
				x = (width - w) / 2;
				break;
			default:
				x = width - w;
				break;
			}
			y = (height - h) / 2;
		} else if (position == BOTTOM_LEFT || position == BOTTOM
				|| position == BOTTOM_RIGHT) {
			switch (position) {
			case BOTTOM_LEFT:
				x = 0;
				break;
			case BOTTOM:
				x = (width - w) / 2;
				break;
			default:
				x = width - w;
				break;
			}
			y = height - h;
		} else {
			x = (width - w) / 2;
			y = (height - h) / 2;
		}

		map.put("x", x);
		map.put("y", y);
		map.put("w", w);
		map.put("h", h);

		return map;
	}

	/**
	 * 缩放图片
	 * 
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	private static void zoom(int w, int h) {
		try {
			// 获取缩放后的Image对象
			Image image = img.getScaledInstance(w, h, Image.SCALE_DEFAULT);
			// 新建一个和Image对象相同大小的画布
			BufferedImage bufferedImage = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			// 获取画笔
			Graphics2D graphics2d = bufferedImage.createGraphics();
			// 将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
			graphics2d.drawImage(image, 0, 0, null);
			// 释放资源
			graphics2d.dispose();
			// 使用ImageIO的方法进行输出,记得关闭资源
			OutputStream outputStream = new FileOutputStream(targetPath);
			ImageIO.write(bufferedImage, targetSuffix, outputStream);
			outputStream.close();

			logger.info("Zoom success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Zoom error");
		}
	}

	/**
	 * 截取图片
	 * 
	 * @param x
	 *            X坐标值
	 * @param y
	 *            Y坐标值
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	private static void cut(int x, int y, int w, int h) {
		try {
			Iterator<ImageReader> iterator = ImageIO
					.getImageReadersByFormatName(sourceSuffix);
			ImageReader imageReader = (ImageReader) iterator.next();
			InputStream inputStream = new FileInputStream(sourcePath);
			ImageInputStream imageInputStream = ImageIO
					.createImageInputStream(inputStream);
			imageReader.setInput(imageInputStream, true);
			ImageReadParam imageReadParam = imageReader.getDefaultReadParam();
			Rectangle rectangle = new Rectangle(x, y, w, h);
			imageReadParam.setSourceRegion(rectangle);
			BufferedImage bufferedImage = imageReader.read(0, imageReadParam);
			// 使用ImageIO的方法进行输出,记得关闭资源
			OutputStream outputStream = new FileOutputStream(targetPath);
			ImageIO.write(bufferedImage, targetSuffix, outputStream);
			outputStream.close();

			logger.info("Cut success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cut error");
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
	public static boolean zoomImage(String picPath, String extName, double scale) {
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
				zoom(_width, _height);

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
	public static boolean zoomImage(String picPath, String filePath,
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
				zoom(_width, _height);

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
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 */
	public static boolean zoomImage(String picPath, String extName, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(extName)
				&& w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				init(picPath, "", extName);
				zoom(w, h);

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
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static boolean zoomImage(String picPath, String filePath,
			String fileName, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(fileName) && w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				file = new File(filePath);

				// 判断目标文件路径是否存在
				if (!file.exists()) {
					file.mkdirs();
				}

				init(picPath, filePath + File.separator + fileName, "");
				zoom(w, h);

				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按位置截取图片
	 * 
	 * @param picPath
	 *            始图片路径
	 * @param extName
	 *            扩展名
	 * @param position
	 *            位置索引
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static boolean cutImage(String picPath, String extName,
			int position, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(extName)
				&& w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				init(picPath, "", extName);
				Map<String, Integer> map = cutImageInfo(position, w, h);
				cut(map.get("x").intValue(), map.get("y").intValue(),
						map.get("w").intValue(), map.get("h").intValue());

				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按位置截取图片
	 * 
	 * @param picPath
	 *            始图片路径
	 * @param filePath
	 *            目标路径
	 * @param fileName
	 *            目标文件名
	 * @param position
	 *            位置索引
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static boolean cutImage(String picPath, String filePath,
			String fileName, int position, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(fileName) && w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				file = new File(filePath);

				// 判断目标文件路径是否存在
				if (!file.exists()) {
					file.mkdirs();
				}

				init(picPath, filePath + File.separator + fileName, "");
				Map<String, Integer> map = cutImageInfo(position, w, h);
				cut(map.get("x").intValue(), map.get("y").intValue(),
						map.get("w").intValue(), map.get("h").intValue());

				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按坐标截取图片
	 * 
	 * @param picPath
	 *            始图片路径
	 * @param extName
	 *            扩展名
	 * @param x
	 *            X坐标值
	 * @param y
	 *            Y坐标值
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static boolean cutImage(String picPath, String extName, int x,
			int y, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(extName)
				&& x >= 0 && y >= 0 && w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				init(picPath, "", extName);
				cut(x, y, w, h);

				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}

	/**
	 * 按坐标截取图片
	 * 
	 * @param picPath
	 *            始图片路径
	 * @param filePath
	 *            目标路径
	 * @param fileName
	 *            目标文件名
	 * @param x
	 *            X坐标值
	 * @param y
	 *            Y坐标值
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @return
	 */
	public static boolean cutImage(String picPath, String filePath,
			String fileName, int x, int y, int w, int h) {
		boolean result = false;

		// 判断参数
		if (StringUtils.isNotEmpty(picPath) && StringUtils.isNotEmpty(filePath)
				&& StringUtils.isNotEmpty(fileName) && x >= 0 && y >= 0
				&& w > 0 && h > 0) {
			File file = new File(picPath);

			// 判断原始文件是否存在
			if (file.exists()) {
				file = new File(filePath);

				// 判断目标文件路径是否存在
				if (!file.exists()) {
					file.mkdirs();
				}

				init(picPath, filePath + File.separator + fileName, "");
				cut(x, y, w, h);

				result = true;
			}
		} else {
			logger.info("Parameter error");
		}

		return result;
	}
}