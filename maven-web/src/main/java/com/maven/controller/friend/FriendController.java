package com.maven.controller.friend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.controller.BaseController;
import com.maven.model.friend.Friend;

/**
 * 好友Controller
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Controller
@RequestMapping(value = "/friend")
public class FriendController extends BaseController {

	/**
	 * 获取好友信息
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
			String friendId = ParamUtils.getStringDefault(request, "friendId",
					"");

			Friend friend = baseService.findById(Friend.class, friendId);

			return ResponseUtils.writeSuccess(friend, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}