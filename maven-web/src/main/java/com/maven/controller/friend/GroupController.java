package com.maven.controller.friend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.request.ParamUtils;
import com.maven.common.request.ResponseUtils;
import com.maven.controller.BaseController;
import com.maven.model.friend.Group;

/**
 * 分组Controller
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Controller
@RequestMapping(value = "/group")
public class GroupController extends BaseController {

	/**
	 * 获取好友分组信息
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
			String groupId = ParamUtils
					.getStringDefault(request, "groupId", "");

			Group group = baseService.findById(Group.class, groupId);

			return ResponseUtils.writeSuccess(group, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}