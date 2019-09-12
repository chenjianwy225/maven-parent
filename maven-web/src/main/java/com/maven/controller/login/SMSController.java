package com.maven.controller.login;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
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
import com.maven.common.RandomUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.UUIDUtils;
import com.maven.controller.BaseController;

/**
 * 短信Controller
 * 
 * @author chenjian
 * @createDate 2019-09-11
 */
@Controller
@RequestMapping(value = "/sms")
public class SMSController extends BaseController {

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
	 * 获取保存Redis的验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/redisCode", method = RequestMethod.GET)
	@ResponseBody
	public Object redisCode(HttpServletRequest request,
			HttpServletResponse response) {
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
			return ResponseUtils.writeSuccess(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}