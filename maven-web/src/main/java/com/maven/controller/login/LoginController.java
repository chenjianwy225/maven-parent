package com.maven.controller.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.authentication.annotation.ValidationAuthority;
import com.maven.common.LoadPropertiesUtils;
import com.maven.common.MD5Utils;
import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.StringUtils;
import com.maven.common.UUIDUtils;
import com.maven.controller.BaseController;
import com.maven.model.sms.Code;
import com.maven.model.user.User;

/**
 * 登录Controller
 * 
 * @author chenjian
 * @createDate 2019-09-10
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {

	// Token在Redis的数据库索引
	private static final int tokenDbIndex = Integer.valueOf(
			LoadPropertiesUtils.getInstance().getKey("tokenDbIndex"))
			.intValue();

	// 验证码在Redis的数据库索引
	private static final int codeDbIndex = Integer.valueOf(
			LoadPropertiesUtils.getInstance().getKey("codeDbIndex")).intValue();

	// Token在Redis的过期时间(分钟)
	private static final long tokenExpireTime = Long.valueOf(
			LoadPropertiesUtils.getInstance().getKey("tokenExpireTime"))
			.longValue();

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isLogin = false;
			String message = "";
			String userName = ParamUtils.getStringDefault(request, "userName",
					"");
			String userPassword = ParamUtils.getStringDefault(request,
					"userPassword", "");
			String userCode = ParamUtils.getStringDefault(request, "userCode",
					"");
			String codeId = ParamUtils.getStringDefault(request, "keyId", "");

			if (StringUtils.isNotEmpty(userName)
					&& StringUtils.isNotEmpty(userPassword)
					&& StringUtils.isNotEmpty(userCode)) {
				String hql = "FROM User WHERE userName = :userName";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userName", userName);

				User user = baseService
						.findByCondition(User.class, hql, params);

				// 判断用户是否存在
				if (StringUtils.isNotEmpty(user)) {
					// 判断密码是否正确
					if (user.getUserPassword().equalsIgnoreCase(
							MD5Utils.encoderToMD5(userPassword))) {
						// 判断是RedisCode或SMSCode
						if (StringUtils.isNotEmpty(codeId)) {
							redisUtil.changeDataBase(codeDbIndex);

							// 判断验证码是否有效
							if (redisUtil.hasKey(codeId)) {
								// 判断验证是否正确
								if (userCode.equalsIgnoreCase(redisUtil.get(
										codeId).toString())) {
									isLogin = true;
								} else {
									message = "验证码错误";
								}
							} else {
								message = "验证码过期";
							}
						} else {
							params.clear();
							hql = "FROM Code WHERE mobile = :mobile AND NOW() BETWEEN startDate AND endDate";
							params.put("mobile", user.getMobile());

							Code code = baseService.findByCondition(Code.class,
									hql, params);

							// 判断验证码是否有效
							if (StringUtils.isNotEmpty(code)) {
								// 判断验证是否正确
								if (userCode
										.equalsIgnoreCase(code.getCodeNum())) {
									isLogin = true;
								} else {
									message = "验证码错误";
								}
							} else {
								message = "验证码过期";
							}
						}

						// 判断是否登录
						if (isLogin) {
							Map<Object, Object> map = BeanUtils.describe(user);
							map.put("authority", "");

							String token = UUIDUtils.getUUID();
							redisUtil.changeDataBase(tokenDbIndex);
							redisUtil.hmset(token, map, tokenExpireTime);
							response.setHeader("token", token);

							message = "登录成功";
						}
					} else {
						message = "密码错误";
					}
				} else {
					message = "用户不存在";
				}
			} else {
				message = "参数错误";
			}

			if (isLogin) {
				return ResponseUtils.writeSuccess(message);
			} else {
				return ResponseUtils.writeFail(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ValidationAuthority(validationToken = true)
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String token = ParamUtils.getStringDefault(request, "token", "");
			if (StringUtils.isEmpty(token)) {
				token = request.getHeader("token");

				if (StringUtils.isEmpty(token)) {
					token = request.getHeader("authorization");
				}
			}

			if (StringUtils.isNotEmpty(token)) {
				redisUtil.changeDataBase(tokenDbIndex);
				redisUtil.del(token);

				return ResponseUtils.writeSuccess("退出登录成功");
			} else {
				return ResponseUtils.writeFail("退出登录失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}