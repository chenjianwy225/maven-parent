package com.maven.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.controller.BaseController;
import com.maven.model.user.User;

/**
 * 用户Controller
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "single", method = RequestMethod.GET)
	@ResponseBody
	public Object single(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = ParamUtils.getStringDefault(request, "userId", "");

			User user = baseService.findById(User.class, userId);
			return ResponseUtils.writeSuccess(user, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public void getAll(HttpServletRequest request, HttpServletResponse response) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}