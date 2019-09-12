package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import net.sf.json.JSONObject;

import com.maven.common.UUIDUtils;
import com.maven.common.http.HttpClientUtils;

public class Ts {

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:9090/sms/redisCode";

		JSONObject result = HttpClientUtils.get(url, null, null);
		System.out.println(result);

		Object object = result.get("data");
		JSONObject json = JSONObject.fromObject(object);
		String base64 = json.getString("base64Img");

		Base64.Decoder decoder = Base64.getDecoder();
		byte[] buff = decoder.decode(base64);
		File file = new File("F:\\" + UUIDUtils.getUUID() + ".png");
		file.createNewFile();

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(buff);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}