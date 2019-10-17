package com.maven.common.url;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.maven.common.http.HttpComponentsUtils;

/**
 * 短网址生成类
 * 
 * @author chenjian
 * @createDate 2019-10-12
 */
public class ShortUrlUtils {

	// 草田签生成短网址地址
	private static final String CTQ_url = "https://ctsign.cn/Web/open/generateShortUrl";

	// 草田签生成短网址秘钥
	private static final String CTQ_secretKey = "7554nbyd864tb4a26vaaf34cba9cqe28e";

	/**
	 * 生成草田签短网址
	 * 
	 * @param url
	 *            原网址
	 * @return 
	 *         JSONObject：{"success":true(成功)或false(失败),"info":信息,"data":{"longUrl"
	 *         :原网址,"shortUrl":生成的短网址,"shortValue":短网址的值,"channelId":渠道ID,
	 *         "createTime":创建时间}}
	 */
	public JSONObject getCTQShortUrl(String url) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("secretKey", CTQ_secretKey);
		params.put("url", url);

		JSONObject jsonObject = HttpComponentsUtils.post(CTQ_url, params, "");
		return jsonObject;
	}
}