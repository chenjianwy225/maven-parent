package com.test;

import com.maven.common.picture.PictureUtils;

public class Ts {

	public static void main(String[] args) throws Exception {
		String picPath = "E:\\images\\1.jpg";

		PictureUtils.zoomToTarget(picPath, "F:\\images", "1.png", 0.5);
		PictureUtils.zoomToTarget(picPath, "F:\\images", "2.png", 800, 600);
	}
}