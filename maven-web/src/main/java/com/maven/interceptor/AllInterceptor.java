package com.maven.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.maven.authentication.annotation.ValidationAuthority;
import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.StringUtils;
import com.maven.common.UUIDUtils;
import com.maven.service.util.RedisUtil;

/**
 * 拦截器
 * 
 * @author chenjian
 * @createDate 2019-09-09
 */
public class AllInterceptor implements HandlerInterceptor {

	private final static Logger logger = LoggerFactory
			.getLogger(AllInterceptor.class);

	@Autowired
	private RedisUtil redisUtil;

	// Redis过期时间(秒)
	private final static long expireTime = 2 * 60 * 60;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean isContinue = true;

		ValidationAuthority validationAuthority = ((HandlerMethod) handler)
				.getMethodAnnotation(ValidationAuthority.class);

		// 判断是否设置验证
		if (StringUtils.isNotEmpty(validationAuthority)) {
			boolean isValidation = validationAuthority.validationToken();
			String[] methodAuthoritys = validationAuthority.value();

			// 判断是否验证Token
			if (isValidation) {
				// 获取用户访问时带的Token参数
				String token = ParamUtils
						.getStringDefault(request, "token", "");
				if (StringUtils.isEmpty(token)) {
					token = request.getHeader("token");

					if (StringUtils.isEmpty(token)) {
						token = request.getHeader("authorization");
					}
				}

				// 判断用户访问是否带Token参数
				if (StringUtils.isNotEmpty(token)) {
					// 判断Token是否有效
					if (StringUtils.isNotEmpty(redisUtil.hasKey(token))) {
						boolean isAuthority = false;
						Map<Object, Object> map = redisUtil.hmget(token);
						List<String> userAuthoritys = StringUtils
								.isNotEmpty(map.get("authority")) ? Arrays
								.asList(map.get("authority").toString())
								: new ArrayList<String>();

						// 判断方法是否设置访问权限
						if (methodAuthoritys.length > 0) {
							for (String methodAuthority : methodAuthoritys) {
								if (userAuthoritys.contains(methodAuthority)) {
									isAuthority = true;
									break;
								}
							}
						} else {
							isAuthority = true;
						}

						// 判断用户是否有访问权限
						if (isAuthority) {
							redisUtil.del(token);
							token = UUIDUtils.getUUID();
							redisUtil.hmset(token, map, expireTime);
							response.setHeader("token", token);
						} else {
							String message = "您沒有权限";
							isContinue = false;

							logger.error(message);
							returnInfo(response, message);
						}
					} else {
						String message = "Token失效";
						isContinue = false;

						logger.error(message);
						returnInfo(response, message);
					}
				} else {
					String message = "无Token";
					isContinue = false;

					logger.error(message);
					returnInfo(response, message);
				}
			}
		}

		return isContinue;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * 构建返回信息
	 * 
	 * @param response
	 * @param message
	 */
	private void returnInfo(HttpServletResponse response, String message) {
		try {
			Object object = ResponseUtils.writeFail(message);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().write(JSONObject.toJSONString(object));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}