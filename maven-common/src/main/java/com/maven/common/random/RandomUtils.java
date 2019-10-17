package com.maven.common.random;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 随机生成类
 * 
 * @author chenjian
 * @createDate 2019-09-11
 */
public class RandomUtils {

	// 字符集合
	private static final String[][] CODES = {
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" },
			{ "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
					"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
					"z" },
			{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
					"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
					"Z" } };

	// 数字字符
	public static final int NUMBER_CODE = 0;

	// 小写字母字符
	public static final int SMALL_LETTER_CODE = 1;

	// 大写字母字符
	public static final int BIG_LETTER_CODE = 2;

	// 每个字符宽度
	private static final int EVERY_WIDTH = 15;

	// 字符串高度
	private static final int HEIGHT = 27;

	/**
	 * 获取验证码
	 * 
	 * @param length
	 *            长度
	 * @param codeIndex
	 *            字符索引
	 * @return
	 */
	public static Object[] getCode(int length, int... codeIndexs) {
		int width = EVERY_WIDTH * length;
		int height = HEIGHT;

		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandomColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", 0, 24));
		g.setColor(getRandomColor(160, 200));

		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = getRandomCode(length, codeIndexs);
		char[] chars = sRand.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			String rand = Character.toString(chars[i]);

			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 24);
		}

		g.dispose();

		return new Object[] { sRand.toUpperCase(), image };
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 *            长度
	 * @param codeIndexs
	 *            字符索引
	 * @return
	 */
	public static String getRandomCode(int length, int... codeIndexs) {
		StringBuffer rand = new StringBuffer();

		// 判断字符串长度
		if (length > 0 && codeIndexs.length > 0) {
			Random random = new Random();
			List<Integer> indexList = new ArrayList<Integer>();

			// 遍历索引集合
			List<String> codeList = new ArrayList<String>();
			for (int codeIndex : codeIndexs) {
				if (!indexList.contains(codeIndex) && codeIndex >= 0
						&& codeIndex < CODES.length) {
					codeList.addAll(Arrays.asList(CODES[codeIndex]));
					indexList.add(codeIndex);
				}
			}

			// 生成字符串
			if (codeList.size() > 0) {
				for (int i = 0; i < length; i++) {
					int index = random.nextInt(codeList.size());

					rand.append(codeList.get(index));
				}
			}
		}

		return rand.toString();
	}

	/**
	 * 生成随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandomColor(int fc, int bc) {
		Random random = new Random();

		if (fc > 255) {
			fc = 255;
		}

		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}