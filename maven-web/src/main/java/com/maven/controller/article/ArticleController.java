package com.maven.controller.article;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.common.DateUtils;
import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.SQLAndParam;
import com.maven.common.StringUtils;
import com.maven.common.page.Pager;
import com.maven.controller.BaseController;
import com.maven.model.article.Article;
import com.maven.model.article.ArticleCollect;
import com.maven.model.article.ArticleComment;
import com.maven.model.article.ArticlePoint;
import com.maven.model.article.ArticleShare;
import com.maven.model.article.ArticleType;
import com.maven.model.user.User;

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

	/**
	 * 搜索文章信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "all", method = RequestMethod.GET)
	@ResponseBody
	public Object all(HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer pageNo = ParamUtils.getIntegerDefault(request,
					"currentPage", 1);
			Integer pageSize = ParamUtils.getIntegerDefault(request,
					"pageSize", 20);

			// SQL查询
			SQLAndParam sqlAndParam = getSQLParam(request);
			Pager pager = baseService.findPageSql(sqlAndParam.getSql(),
					sqlAndParam.getParamList().toArray(), pageNo, pageSize);

			return ResponseUtils.writeSuccess(pager, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * SQL语句和参数
	 * 
	 * @param request
	 * @return
	 */
	private SQLAndParam getSQLParam(HttpServletRequest request) {
		String keyword = ParamUtils.getStringDefault(request, "keyword", "");
		String typeId = ParamUtils.getStringDefault(request, "typeId", "");
		String beginDate = ParamUtils
				.getStringDefault(request, "beginDate", "");
		String endDate = ParamUtils.getStringDefault(request, "endDate", "");

		StringBuffer sql = new StringBuffer();

		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT a.title, a.summary, a.content, a.picPath, a.audioPath, a.videoPath, a.hits, a.createDate");
		sql.append(", (SELECT COUNT(0) FROM " + ArticleComment.tableName
				+ " WHERE articleId = a.keyId) AS commentNum");
		sql.append(", (SELECT COUNT(0) FROM " + ArticleCollect.tableName
				+ " WHERE articleId = a.keyId) AS collectNum");
		sql.append(", (SELECT COUNT(0) FROM " + ArticlePoint.tableName
				+ " WHERE articleId = a.keyId) AS pointNum");
		sql.append(", (SELECT COUNT(0) FROM " + ArticleShare.tableName
				+ " WHERE articleId = a.keyId) AS shareNum");
		sql.append(", at.name AS typeName, u.nickName");
		sql.append(" FROM " + Article.tableName + " a");
		sql.append(" LEFT JOIN " + ArticleType.tableName
				+ " at ON at.keyId = a.typeId");
		sql.append(" LEFT JOIN " + User.tableName + " u ON u.keyId = a.userId");

		StringBuffer where = new StringBuffer();
		if (StringUtils.isNotEmpty(keyword)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("(a.title LIKE ? OR a.summary LIKE ? OR a.content LIKE ? OR u.nickName LIKE ?)");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		}

		if (StringUtils.isNotEmpty(typeId)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("a.typeId = ?");
			params.add(typeId);
		}

		if (StringUtils.isNotEmpty(beginDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("a.createDate >= ?");
			params.add(DateUtils.strToDate(beginDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("a.createDate <= ?");
			params.add(DateUtils.strToDate(endDate, "yyyy-MM-dd"));
		}
		sql.append(where);
		sql.append(where.toString().equalsIgnoreCase("") ? " WHERE " : " AND ");
		sql.append(" a.status = ?");
		params.add("1");

		sql.append(" ORDER BY a.createDate DESC");

		SQLAndParam sqlAndParam = new SQLAndParam();
		sqlAndParam.setSql(sql.toString());
		sqlAndParam.setParamList(params);

		return sqlAndParam;
	}
}