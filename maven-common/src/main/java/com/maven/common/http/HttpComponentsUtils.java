package com.maven.common.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * HttpComponents服务请求类
 * 
 * @author chenjian
 * @createDate 2019-09-10
 */
public class HttpComponentsUtils {

	private static Logger logger = LoggerFactory
			.getLogger(HttpComponentsUtils.class);

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
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(url)) {
				httpClient = HttpClientBuilder.create().build();

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
										entry.getValue().toString(), "utf-8");
					}

					url += "?" + paramUrl.substring(1);
				}

				httpGet = new HttpGet(url);

				// 配置信息
				RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
						.setConnectTimeout(5000)
						// 设置请求超时时间(单位毫秒)
						.setConnectionRequestTimeout(5000)
						// socket读写超时时间(单位毫秒)
						.setSocketTimeout(5000)
						// 设置是否允许重定向(默认为true)
						.setRedirectsEnabled(true).build();

				// 将上面的配置信息 运用到这个Get请求里
				httpGet.setConfig(requestConfig);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					httpGet.setHeader("token", token);
				}

				// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
				httpGet.setHeader("Content-Type",
						"application/json;charset=UTF-8");

				// 由客户端执行(发送)Get请求
				response = httpClient.execute(httpGet);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// 从响应模型中获取响应实体
					HttpEntity responseEntity = response.getEntity();

					if (StringUtils.isNotEmpty(responseEntity)) {
						Header[] headers = response.getHeaders("token");

						if (headers.length > 0) {
							token = headers[0].getValue();
						}

						result = JSONObject.fromObject(EntityUtils
								.toString(responseEntity));

						if (StringUtils.isNotEmpty(token)) {
							result.put("token", token);
						}
					}
				}

				logger.info("Get request success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request success");
		} finally {
			try {
				if (StringUtils.isNotEmpty(httpClient)) {
					httpClient.close();
				}

				if (StringUtils.isNotEmpty(response)) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Get请求
	 * 
	 * @param scheme
	 *            请求Scheme('http'或'https')
	 * @param host
	 *            请求地址
	 * @param port
	 *            请求端口
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param token
	 *            Token值
	 * @return
	 */
	public static JSONObject get(String scheme, String host, int port,
			String path, Map<String, Object> params, String token) {
		JSONObject result = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;
		URI uri = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(scheme) && StringUtils.isNotEmpty(host)
					&& port > 0 && StringUtils.isNotEmpty(path)) {
				httpClient = HttpClientBuilder.create().build();
				URIBuilder builder = new URIBuilder();
				builder.setScheme(scheme);
				builder.setHost(host);
				builder.setPort(port);
				builder.setPath(path);

				// 判断是否有参数
				if (StringUtils.isNotEmpty(params) && params.size() > 0) {
					List<NameValuePair> nameValuePair = new ArrayList<>();
					Iterator<Entry<String, Object>> iterator = params
							.entrySet().iterator();

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();

						nameValuePair.add(new BasicNameValuePair(
								entry.getKey(), entry.getValue().toString()));
					}

					builder.setParameters(nameValuePair);
				}

				uri = builder.build();
				httpGet = new HttpGet(uri);

				// 配置信息
				RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
						.setConnectTimeout(5000)
						// 设置请求超时时间(单位毫秒)
						.setConnectionRequestTimeout(5000)
						// socket读写超时时间(单位毫秒)
						.setSocketTimeout(5000)
						// 设置是否允许重定向(默认为true)
						.setRedirectsEnabled(true).build();

				// 将上面的配置信息 运用到这个Get请求里
				httpGet.setConfig(requestConfig);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					httpGet.setHeader("token", token);
				}

				// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
				httpGet.setHeader("Content-Type",
						"application/json;charset=UTF-8");

				// 由客户端执行(发送)Get请求
				response = httpClient.execute(httpGet);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// 从响应模型中获取响应实体
					HttpEntity responseEntity = response.getEntity();

					if (StringUtils.isNotEmpty(responseEntity)) {
						Header[] headers = response.getHeaders("token");

						if (headers.length > 0) {
							token = headers[0].getValue();
						}

						result = JSONObject.fromObject(EntityUtils
								.toString(responseEntity));

						if (StringUtils.isNotEmpty(token)) {
							result.put("token", token);
						}
					}
				}

				logger.info("Get request success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(httpClient)) {
					httpClient.close();
				}

				if (StringUtils.isNotEmpty(response)) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
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
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(url)) {
				httpClient = HttpClientBuilder.create().build();

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
										entry.getValue().toString(), "utf-8");
					}

					url += "?" + paramUrl.substring(1);
				}

				httpPost = new HttpPost(url);

				// 配置信息
				RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
						.setConnectTimeout(5000)
						// 设置请求超时时间(单位毫秒)
						.setConnectionRequestTimeout(5000)
						// socket读写超时时间(单位毫秒)
						.setSocketTimeout(5000)
						// 设置是否允许重定向(默认为true)
						.setRedirectsEnabled(true).build();

				// 将上面的配置信息 运用到这个Post请求里
				httpPost.setConfig(requestConfig);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					httpPost.setHeader("token", token);
				}

				// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
				httpPost.setHeader("Content-Type",
						"application/json;charset=UTF-8");

				// 由客户端执行(发送)Post请求
				response = httpClient.execute(httpPost);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// 从响应模型中获取响应实体
					HttpEntity responseEntity = response.getEntity();

					if (StringUtils.isNotEmpty(responseEntity)) {
						Header[] headers = response.getHeaders("token");

						if (headers.length > 0) {
							token = headers[0].getValue();
						}

						result = JSONObject.fromObject(EntityUtils
								.toString(responseEntity));

						if (StringUtils.isNotEmpty(token)) {
							result.put("token", token);
						}
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
			try {
				if (StringUtils.isNotEmpty(httpClient)) {
					httpClient.close();
				}

				if (StringUtils.isNotEmpty(response)) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Post请求
	 * 
	 * @param scheme
	 *            请求Scheme('http'或'https')
	 * @param host
	 *            请求地址
	 * @param port
	 *            请求端口
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param token
	 *            Token值
	 * @return
	 */
	public static JSONObject post(String scheme, String host, int port,
			String path, Map<String, Object> params, String token) {
		JSONObject result = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = null;
		URI uri = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(scheme) && StringUtils.isNotEmpty(host)
					&& port > 0 && StringUtils.isNotEmpty(path)) {
				httpClient = HttpClientBuilder.create().build();
				URIBuilder builder = new URIBuilder();
				builder.setScheme(scheme);
				builder.setHost(host);
				builder.setPort(port);
				builder.setPath(path);

				// 判断是否有参数
				if (StringUtils.isNotEmpty(params) && params.size() > 0) {
					List<NameValuePair> nameValuePair = new ArrayList<>();
					Iterator<Entry<String, Object>> iterator = params
							.entrySet().iterator();

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();

						nameValuePair.add(new BasicNameValuePair(
								entry.getKey(), entry.getValue().toString()));
					}

					builder.setParameters(nameValuePair);
				}

				uri = builder.build();
				httpPost = new HttpPost(uri);

				// 配置信息
				RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
						.setConnectTimeout(5000)
						// 设置请求超时时间(单位毫秒)
						.setConnectionRequestTimeout(5000)
						// socket读写超时时间(单位毫秒)
						.setSocketTimeout(5000)
						// 设置是否允许重定向(默认为true)
						.setRedirectsEnabled(true).build();

				// 将上面的配置信息 运用到这个Post请求里
				httpPost.setConfig(requestConfig);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					httpPost.setHeader("token", token);
				}

				// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
				httpPost.setHeader("Content-Type",
						"application/json;charset=UTF-8");

				// 由客户端执行(发送)Get请求
				response = httpClient.execute(httpPost);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// 从响应模型中获取响应实体
					HttpEntity responseEntity = response.getEntity();

					if (StringUtils.isNotEmpty(responseEntity)) {
						Header[] headers = response.getHeaders("token");

						if (headers.length > 0) {
							token = headers[0].getValue();
						}

						result = JSONObject.fromObject(EntityUtils
								.toString(responseEntity));

						if (StringUtils.isNotEmpty(token)) {
							result.put("token", token);
						}
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
			try {
				if (StringUtils.isNotEmpty(httpClient)) {
					httpClient.close();
				}

				if (StringUtils.isNotEmpty(response)) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Upload请求
	 * 
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param token
	 *            Token值
	 * @return
	 */
	public static JSONObject upload(String url, Map<String, Object> params,
			String token) {
		JSONObject result = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpPost httpPost = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(url)) {
				httpClient = HttpClientBuilder.create().build();
				httpPost = new HttpPost(url);

				// 配置信息
				RequestConfig requestConfig = RequestConfig.custom()
				// 设置连接超时时间(单位毫秒)
						.setConnectTimeout(5000)
						// 设置请求超时时间(单位毫秒)
						.setConnectionRequestTimeout(5000)
						// socket读写超时时间(单位毫秒)
						.setSocketTimeout(15000).build();

				// 将上面的配置信息 运用到这个Post请求里
				httpPost.setConfig(requestConfig);

				// 设置Token到Header
				if (StringUtils.isNotEmpty(token)) {
					httpPost.setHeader("token", token);
				}

				MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
						.create();

				// 判断是否有参数
				if (StringUtils.isNotEmpty(params) && params.size() > 0) {
					Iterator<Entry<String, Object>> iterator = params
							.entrySet().iterator();

					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();
						String key = entry.getKey();
						Object object = entry.getValue();

						ContentBody contentBody = null;
						if (object instanceof File) {
							contentBody = new FileBody((File) object);
						} else if (object instanceof InputStream) {
							contentBody = new InputStreamBody(
									(InputStream) object, key);
						} else if (object instanceof String) {
							contentBody = new StringBody(object.toString(),
									ContentType.MULTIPART_FORM_DATA);
						}

						if (StringUtils.isNotEmpty(contentBody)) {
							multipartEntityBuilder.addPart(key, contentBody);
						}
					}
				}

				HttpEntity httpEntity = multipartEntityBuilder.build();
				httpPost.setEntity(httpEntity);

				// 由客户端执行(发送)Post请求
				response = httpClient.execute(httpPost);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					// 从响应模型中获取响应实体
					HttpEntity responseEntity = response.getEntity();

					if (StringUtils.isNotEmpty(responseEntity)) {
						Header[] headers = response.getHeaders("token");

						if (headers.length > 0) {
							token = headers[0].getValue();
						}

						result = JSONObject.fromObject(EntityUtils
								.toString(responseEntity));

						if (StringUtils.isNotEmpty(token)) {
							result.put("token", token);
						}
					}
				}

				logger.info("Upload request success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Upload request error");
		} finally {
			try {
				if (StringUtils.isNotEmpty(httpClient)) {
					httpClient.close();
				}

				if (StringUtils.isNotEmpty(response)) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}