package com.maven.controller.login;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.LoadPropertiesUtils;
import com.maven.common.ParamUtils;
import com.maven.common.RandomUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.StringUtils;
import com.maven.common.UUIDUtils;
import com.maven.controller.BaseController;
import com.maven.model.sms.Code;

/**
 * 验证码Controller
 * 
 * @author chenjian
 * @createDate 2019-09-11
 */
@Controller
@RequestMapping(value = "/code")
public class CodeController extends BaseController {

	// 验证码长度
	private static final int codeLengths = Integer.valueOf(
			LoadPropertiesUtils.getInstance().getKey("codeLengths")).intValue();

	// 验证码在Redis的数据库索引
	private static final int codeDbIndex = Integer.valueOf(
			LoadPropertiesUtils.getInstance().getKey("codeDbIndex")).intValue();

	// 验证码在Redis的过期时间(分钟)
	private static final long codeExpireTime = Long.valueOf(
			LoadPropertiesUtils.getInstance().getKey("codeExpireTime"))
			.longValue();

	/**
	 * 生成Redis验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	@ResponseBody
	public Object redis(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codeId = UUIDUtils.getUUID();
			Object[] obj = RandomUtils.getCode(codeLengths,
					RandomUtils.NUMBER_CODE, RandomUtils.SMALL_LETTER_CODE,
					RandomUtils.BIG_LETTER_CODE);

			BufferedImage image = (BufferedImage) obj[1];
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", outputStream);

			Base64.Encoder encoder = Base64.getEncoder();
			String base64Img = encoder.encodeToString(outputStream
					.toByteArray());
			// base64Img = "data:image/jpg;base64," + base64Img;

			redisUtil.changeDataBase(codeDbIndex);
			redisUtil.set(codeId, obj[0], codeExpireTime);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("codeId", codeId);
			map.put("base64Img", base64Img);
			return ResponseUtils.writeSuccess("发送成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * 生成并发送短信验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	@ResponseBody
	public Object sms(HttpServletRequest request, HttpServletResponse response) {
		try {
			String message = "";
			boolean isAdd = false;
			String mobile = ParamUtils.getStringDefault(request, "mobile", "");

			// 判断是否填写手机号码
			if (StringUtils.isNotEmpty(mobile)) {
				Code code = baseService.findById(Code.class, mobile);

				// 判断数据是否存在
				if (StringUtils.isEmpty(code)) {
					code = new Code();
					code.setMobile(mobile);

					isAdd = true;
				}

				// 验证码
				String codeNum = RandomUtils.getRandomCode(codeLengths,
						RandomUtils.NUMBER_CODE);

				Calendar calendar = Calendar.getInstance();
				int expireTime = Long.valueOf(codeExpireTime).intValue();
				Date startDate = calendar.getTime();
				calendar.add(Calendar.MINUTE, expireTime);
				Date endDate = calendar.getTime();

				code.setCodeNum(codeNum);
				code.setNumber(code.getNumber() + 1);
				code.setExpireTime(expireTime);
				code.setStartDate(startDate);
				code.setEndDate(endDate);

				// 判断是否新增数据
				if (isAdd) {
					baseService.save(code);
				} else {
					baseService.update(code);
				}

				message = "发送成功";
			} else {
				message = "未填写手机号码";
			}

			return ResponseUtils.writeSuccess(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}