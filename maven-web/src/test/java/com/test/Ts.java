package com.test;

import com.maven.common.image.ImageUtils;

public class Ts {

	public static void main(String[] args) throws Exception {
		String picPath = "E:\\images\\1.jpg";
		String filePath = "F:\\images";

		ImageUtils.cutImage(picPath, "b", ImageUtils.CENTER, 800, 600);
		ImageUtils.cutImage(picPath, "c", 200, 200, 800, 600);
	}
}