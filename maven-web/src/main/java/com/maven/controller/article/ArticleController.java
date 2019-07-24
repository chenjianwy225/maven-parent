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
import com.maven.model.article.Article;

/**
 * 文章Controller
 * 
 * @author chenjian
 * @createDate 2019-07-24
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController extends BaseController {

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
			String articleId = ParamUtils.getStringDefault(request,
					"articleId", "");

			Article article = baseService.findById(Article.class, articleId);
			return ResponseUtils.writeSuccess(article, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}
}