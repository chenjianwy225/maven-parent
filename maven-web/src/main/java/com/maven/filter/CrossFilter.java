package com.maven.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maven.common.StringUtils;

/**
 * 跨域过滤器类
 * 
 * @author chenjian
 * @createDate 2019-01-03
 */
public class CrossFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String orgin = req.getHeader("Origin");
		String headers = req.getHeader("Access-Control-Request-Headers");

		if (StringUtils.isNotEmpty(orgin)) {
			res.setHeader("Access-Control-Allow-Origin", orgin);
		}

		if (StringUtils.isNotEmpty(headers)) {
			res.setHeader("Access-Control-Allow-Headers", headers);
		}
		res.setHeader("Access-Control-Allow-Methods",
				"POST, GET, PUT, OPTIONS, DELETE");
		res.addHeader("Access-Control-Max-Age", "3600");
		res.addHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}