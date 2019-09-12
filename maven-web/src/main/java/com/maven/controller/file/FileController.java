package com.maven.controller.file;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.maven.common.InputStreamUtils;
import com.maven.common.LoadPropertiesUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.StringUtils;
import com.maven.common.UUIDUtils;
import com.maven.controller.BaseController;

/**
 * 文件Controller
 * 
 * @author chenjian
 * @createDate 2019-09-11
 */
@Controller
@RequestMapping(value = "/file")
public class FileController extends BaseController {

	// File在Redis的数据库索引
	private static final int fileDbIndex = Integer.valueOf(
			LoadPropertiesUtils.getInstance().getKey("fileDbIndex")).intValue();

	// File在Redis的过期时间(分钟)
	private static final long fileExpireTime = Long.valueOf(
			LoadPropertiesUtils.getInstance().getKey("fileExpireTime"))
			.longValue();

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 * @param file
	 *            文件
	 * @param fileType
	 *            文件类型
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file, String fileType) {
		try {
			String message = "";
			boolean isSuccess = true;
			Map<String, Object> resultObj = null;

			// 判断是否上传文件
			if (StringUtils.isNotEmpty(file)
					&& StringUtils.isNotEmpty(fileType)) {
				String fileId = UUIDUtils.getUUID();
				String fileName = fileId;

				if (fileType.equalsIgnoreCase("image")) {
					fileName += ".png";
				} else if (fileType.equalsIgnoreCase("audio")) {
					fileName += ".mp3";
				} else if (fileType.equalsIgnoreCase("video")) {
					fileName += ".mp4";
				} else {
					isSuccess = false;
					message = "不支持文件类型";
				}

				// 判断是否将文件写入Redis
				if (isSuccess) {
					byte[] byt = InputStreamUtils.inputStreamToByte(file
							.getInputStream());

					Map<Object, Object> map = new HashMap<Object, Object>();
					map.put("file", byt);
					map.put("name", fileName);
					redisUtil.changeDataBase(fileDbIndex);
					redisUtil.hmset(fileId, map, fileExpireTime);

					resultObj = new HashMap<String, Object>();
					resultObj.put("fileId", fileId);

					message = "上传成功";
				}
			} else {
				isSuccess = false;
				message = "参数错误";
			}

			if (isSuccess) {
				return ResponseUtils.writeSuccess(message, resultObj);
			} else {
				return ResponseUtils.writeFail(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}