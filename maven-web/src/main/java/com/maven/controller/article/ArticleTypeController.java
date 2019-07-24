package com.maven.controller.article;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.controller.BaseController;
import com.maven.model.article.ArticleType;

/**
 * 文章分类Controller
 * 
 * @author chenjian
 * @createDate 2019-07-24
 */
@Controller
@RequestMapping(value = "/articletype")
public class ArticleTypeController extends BaseController {

	/**
	 * 获取文章信息
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
			String typeId = ParamUtils.getStringDefault(request, "typeId", "");

			ArticleType articleType = baseService.findById(ArticleType.class,
					typeId);
			return ResponseUtils.writeSuccess(articleType, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}