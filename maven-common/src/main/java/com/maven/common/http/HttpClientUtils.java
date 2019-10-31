package com.maven.common.http;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * HttpClient服务请求类
 * 
 * @author chenjian
 * @createDate 2019-09-10
 */
public class HttpClientUtils {

	private static Logger logger = LoggerFactory
			.getLogger(HttpClientUtils.class);

	// 字符编码
	private static final String ENCODING = "UTF-8";

	/**
	 * Get请求
	 * 
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param token
	 *            Token值
	 * @return
	 */
	public static JSONObject get(String url, Map<String, Object> params,
			String token) {
		JSONObject result = null;
		HttpClient client = null;
		GetMethod method = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(url)) {
				client = new HttpClient();

				// 判断是否有参数
				if (StringUtils.isNotEmpty(params) && params.size() > 0) {
					Iterator<Entry<String, Object>> iterator = params
							.entrySet().iterator();

					String paramUrl = "";
					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();

						paramUrl += "&"
								+ entry.getKey()
								+ "="
								+ URLEncoder.encode(
										entry.getValue().toString(), "UTF-8");
					}

					url += "?" + paramUrl.substring(1);
				}

				method = new GetMethod(url);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					method.addRequestHeader("token", token);
				}
				method.addRequestHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");

				int statusCode = client.executeMethod(method);

				// 判断服务器返回的状态
				if (statusCode == HttpStatus.SC_OK) {
					if (StringUtils.isNotEmpty(method
							.getResponseHeader("token"))) {
						token = method.getResponseHeader("token").getValue();
					}

					// 返回响应消息
					byte[] responseBody = method.getResponseBodyAsString()
							.getBytes(method.getResponseCharSet());

					// 在返回响应消息使用编码(UTF-8或GB2312)
					result = JSONObject.fromObject(new String(responseBody,
							ENCODING));

					if (StringUtils.isNotEmpty(token)) {
						result.put("token", token);
					}
				}

				logger.info("Get request success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Get request error");
		} finally {
			// 释放连接
			if (StringUtils.isNotEmpty(method)) {
				method.releaseConnection();
			}
		}
		return result;
	}

	/**
	 * Post请求
	 * 
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param token
	 *            Token值
	 * @return
	 */
	public static JSONObject post(String url, Map<String, Object> params,
			String token) {
		JSONObject result = null;
		HttpClient client = null;
		PostMethod method = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(url)) {
				client = new HttpClient();
				method = new PostMethod(url);

				// 判断是否有参数
				if (StringUtils.isNotEmpty(params) && params.size() > 0) {
					Iterator<Entry<String, Object>> iterator = params
							.entrySet().iterator();
					NameValuePair[] nameValuePair = new NameValuePair[params
							.size()];
					int index = 0;

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();

						nameValuePair[index] = new NameValuePair(
								entry.getKey(), entry.getValue().toString());
						index += 1;
					}

					method.setRequestBody(nameValuePair);
				}

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					method.addRequestHeader("token", token);
				}
				method.addRequestHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");

				int statusCode = client.executeMethod(method);

				// 判断服务器返回的状态
				if (statusCode == HttpStatus.SC_OK) {
					if (StringUtils.isNotEmpty(method
							.getResponseHeader("token"))) {
						token = method.getResponseHeader("token").getValue();
					}

					// 返回响应消息
					byte[] responseBody = method.getResponseBodyAsString()
							.getBytes(method.getResponseCharSet());

					// 在返回响应消息使用编码(UTF-8或GB2312)
					result = JSONObject.fromObject(new String(responseBody,
							ENCODING));

					if (StringUtils.isNotEmpty(token)) {
						result.put("token", token);
					}
				}

				logger.info("Post request success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Post request error");
		} finally {
			// 释放连接
			if (StringUtils.isNotEmpty(method)) {
				method.releaseConnection();
			}
		}
		return result;
	}
}