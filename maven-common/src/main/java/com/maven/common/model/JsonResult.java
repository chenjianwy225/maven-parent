package com.maven.common.model;

/**
 * 返回信息实体类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public class JsonResult {

	// 返回信息
	private String msg = "请求失败";

	// 返回编码
	private int code = -1;

	// 是否成功
	private Boolean success = false;

	// 返回数据
	private Object data = null;

	// 是否登录
	private Boolean isLogin = false;

	// 图片访问路径
	private String fileUrl = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}