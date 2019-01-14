package com.maven.common;

import com.maven.common.model.JsonResult;

/**
 * 返回信息类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public class ResponseUtils {

	// 图片访问路径
	@SuppressWarnings("static-access")
	private static final String fileUrl = LoadPropertiesUtils.getInstance()
			.getKey("fileUrl");

	/**
	 * 获取成功信息
	 * 
	 * @param data
	 *            数据信息
	 * @param isLogin
	 *            是否登录
	 * @return
	 */
	public static Object writeSuccess(Object data, boolean isLogin) {
		return writeSuccess("请求成功", 0, data, isLogin, fileUrl);
	}

	/**
	 * 获取成功信息
	 * 
	 * @param msg
	 *            信息内容
	 * @param code
	 *            信息编码
	 * @param isLogin
	 *            是否登录
	 * @return
	 */
	public static Object writeSuccess(String msg, int code, boolean isLogin) {
		return writeSuccess(msg, code, null, isLogin, fileUrl);
	}

	/**
	 * 获取成功信息
	 * 
	 * @param msg
	 *            信息内容
	 * @param code
	 *            信息编码
	 * @param data
	 *            数据信息
	 * @param isLogin
	 *            是否登录
	 * @return
	 */
	public static Object writeSuccess(String msg, int code, Object data,
			boolean isLogin) {
		return writeSuccess(msg, code, data, isLogin, fileUrl);
	}

	/**
	 * 获取成功信息
	 * 
	 * @param msg
	 *            信息内容
	 * @param code
	 *            信息编码
	 * @param data
	 *            数据信息
	 * @param isLogin
	 *            是否登录
	 * @param fileUrl
	 *            图片访问路径
	 * @return
	 */
	public static Object writeSuccess(String msg, int code, Object data,
			boolean isLogin, String fileUrl) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg(msg);
		jsonResult.setSuccess(true);
		jsonResult.setCode(code);
		jsonResult.setData(data);
		jsonResult.setIsLogin(isLogin);
		jsonResult.setFileUrl(fileUrl);
		return jsonResult;
	}

	/**
	 * 获取失败信息
	 * 
	 * @return
	 */
	public static Object writeFail() {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg("请求失败");
		return jsonResult;
	}

	/**
	 * 获取失败信息
	 * 
	 * @param msg
	 *            信息内容
	 * @return
	 */
	public static Object writeFail(String msg) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg(msg);
		return jsonResult;
	}
}