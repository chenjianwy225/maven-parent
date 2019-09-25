package com.test;

import com.alibaba.fastjson.JSONObject;
import com.maven.common.xml.XMLUtils;

public class Ts {

	public static void main(String[] args) throws Exception {
		String filePath = "F:\\1.xml";

		JSONObject jsonObject = XMLUtils.read(filePath);
		System.out.println(jsonObject);

		XMLUtils.write("F:\\2.xml", jsonObject);

		byte[] byt = XMLUtils.write(jsonObject);
		System.out.println(byt.length);
	}
}