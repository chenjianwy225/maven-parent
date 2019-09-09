package com.maven.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
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

	private final static long timeout = 2 * 60 * 60;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean isContinue = true;
		String token = request.getHeader("token");

		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("authorization");
		}

		if (StringUtils.isNotEmpty(token)) {
			if (StringUtils.isNotEmpty(redisUtil.hasKey(token))) {
				Map<Object, Object> map = redisUtil.hmget(token);
				token = UUIDUtils.getUUID();
				redisUtil.hmset(token, map, timeout);
				response.setHeader("token", token);
			} else {
				String message = "Token失效";
				isContinue = false;

				logger.error(message);
				returnInfo(response, message);
			}
		} else {
			String message = "Token无效";
			isContinue = false;

			logger.error(message);
			returnInfo(response, message);
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