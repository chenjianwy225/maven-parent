package com.maven.controller.file;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.maven.common.ResponseUtils;
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

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 * @param file
	 *            文件
	 * @param fileType
	 *            文件类型
	 * @param fileSuffix
	 *            文件后缀名
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest request,
			HttpServletResponse response, MultipartFile file, String fileType,
			String fileSuffix) {
		try {

			return ResponseUtils.writeSuccess("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}