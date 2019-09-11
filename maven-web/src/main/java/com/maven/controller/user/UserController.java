package com.maven.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.authentication.annotation.ValidationAuthority;
import com.maven.common.DateUtils;
import com.maven.common.MD5Utils;
import com.maven.common.ParamUtils;
import com.maven.common.ResponseUtils;
import com.maven.common.SQLAndParam;
import com.maven.common.StringUtils;
import com.maven.common.UUIDUtils;
import com.maven.common.page.Pager;
import com.maven.controller.BaseController;
import com.maven.model.user.User;
import com.maven.model.user.UserInfo;

/**
 * 用户Controller
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ValidationAuthority(validationToken = true)
	@RequestMapping(value = "single", method = RequestMethod.GET)
	@ResponseBody
	public Object single(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = ParamUtils.getStringDefault(request, "userId", "");

			User user = baseService.findById(User.class, userId);
			return ResponseUtils.writeSuccess(user, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * 搜索用户信息
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

			// HQL查询(Param为List)
			// SQLAndParam sqlAndParam = getHQLParamList(request);
			//
			// Pager pager = baseService.findPage(User.class,
			// sqlAndParam.getSql(), sqlAndParam.getParamList().toArray(),
			// pageNo, pageSize);

			// HQL查询(Param为Map)
			// SQLAndParam sqlAndParam = getHQLParamMap(request);
			//
			// Pager pager = baseService.findPage(User.class,
			// sqlAndParam.getSql(), sqlAndParam.getParamMap(), pageNo,
			// pageSize);

			return ResponseUtils.writeSuccess(pager, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * 编辑用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userId = ParamUtils.getStringDefault(request, "userId", "");
			String userName = ParamUtils.getStringDefault(request, "userName",
					"");
			String userPassword = ParamUtils.getStringDefault(request,
					"userPassword", "");
			String mobile = ParamUtils.getStringDefault(request, "mobile", "");
			String nickName = ParamUtils.getStringDefault(request, "nickName",
					"");
			String trueName = ParamUtils.getStringDefault(request, "trueName",
					"");
			String sex = ParamUtils.getStringDefault(request, "sex", "");
			String idType = ParamUtils.getStringDefault(request, "idType", "");
			String idNo = ParamUtils.getStringDefault(request, "idNo", "");
			String idiograph = ParamUtils.getStringDefault(request,
					"idiograph", "");
			String occupation = ParamUtils.getStringDefault(request,
					"occupation", "");
			String age = ParamUtils.getStringDefault(request, "age", "");
			String birthday = ParamUtils.getStringDefault(request, "birthday",
					"");
			String constellation = ParamUtils.getStringDefault(request,
					"constellation", "");
			String degree = ParamUtils.getStringDefault(request, "degree", "");
			String school = ParamUtils.getStringDefault(request, "school", "");
			String company = ParamUtils
					.getStringDefault(request, "company", "");
			String address = ParamUtils
					.getStringDefault(request, "address", "");

			String message = "";
			User user = null;
			UserInfo userInfo = null;
			if (StringUtils.isEmpty(userId)) {
				if (StringUtils.isNotEmpty(userName)
						&& StringUtils.isNotEmpty(userPassword)
						&& StringUtils.isNotEmpty(mobile)
						&& StringUtils.isNotEmpty(nickName)) {
					user = new User();
					userInfo = new UserInfo();

					user.setKeyId(UUIDUtils.getUUID());
					userInfo.setUserId(user.getKeyId());

					message = "添加成功";
				} else {
					message = "参数错误";
				}
			} else {
				user = baseService.findById(User.class, userId);
				userInfo = user.getUserInfo();

				user.setModifyDate(new Date());

				message = "修改成功";
			}

			if (StringUtils.isNotEmpty(user)) {
				if (StringUtils.isNotEmpty(userName)) {
					user.setUserName(userName);
				}

				if (StringUtils.isNotEmpty(userPassword)) {
					user.setUserPassword(MD5Utils.encoderToMD5(userPassword));
				}

				if (StringUtils.isNotEmpty(mobile)) {
					user.setMobile(mobile);
				}

				if (StringUtils.isNotEmpty(nickName)) {
					user.setNickName(nickName);
				}

				if (StringUtils.isNotEmpty(trueName)) {
					user.setTrueName(trueName);
				}

				if (StringUtils.isNotEmpty(sex)) {
					user.setSex(sex);
				}

				if (StringUtils.isNotEmpty(idType)) {
					userInfo.setIdType(idType);
				}

				if (StringUtils.isNotEmpty(idNo)) {
					userInfo.setIdNo(idNo);
				}

				if (StringUtils.isNotEmpty(idiograph)) {
					userInfo.setIdiograph(idiograph);
				}

				if (StringUtils.isNotEmpty(occupation)) {
					userInfo.setOccupation(occupation);
				}

				if (StringUtils.isNotEmpty(age)) {
					userInfo.setAge(Integer.parseInt(age));
				}

				if (StringUtils.isNotEmpty(birthday)) {
					userInfo.setBirthday(DateUtils.strToDate(birthday,
							"yyyy-MM-dd"));
				}

				if (StringUtils.isNotEmpty(constellation)) {
					userInfo.setConstellation(constellation);
				}

				if (StringUtils.isNotEmpty(degree)) {
					userInfo.setDegree(degree);
				}

				if (StringUtils.isNotEmpty(school)) {
					userInfo.setSchool(school);
				}

				if (StringUtils.isNotEmpty(company)) {
					userInfo.setCompany(company);
				}

				if (StringUtils.isNotEmpty(address)) {
					userInfo.setAddress(address);
				}

				user.setUserInfo(userInfo);
				baseService.saveOrUpdate(user);
			}

			return ResponseUtils.writeSuccess(message, true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.writeFail();
		}
	}

	/**
	 * 删除用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = ParamUtils.getStringDefault(request, "userId", "");

			baseService.delete(User.class, userId);

			return ResponseUtils.writeSuccess("删除成功", true);
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
		String sex = ParamUtils.getStringDefault(request, "sex", "");
		String idNo = ParamUtils.getStringDefault(request, "idNo", "");
		String occupation = ParamUtils.getStringDefault(request, "occupation",
				"");
		String minAge = ParamUtils.getStringDefault(request, "minAge", "");
		String maxAge = ParamUtils.getStringDefault(request, "maxAge", "");
		String beginDate = ParamUtils
				.getStringDefault(request, "beginDate", "");
		String endDate = ParamUtils.getStringDefault(request, "endDate", "");
		String constellation = ParamUtils.getStringDefault(request,
				"constellation", "");
		String degree = ParamUtils.getStringDefault(request, "degree", "");
		String school = ParamUtils.getStringDefault(request, "school", "");
		String company = ParamUtils.getStringDefault(request, "company", "");
		String address = ParamUtils.getStringDefault(request, "address", "");

		StringBuffer sql = new StringBuffer();

		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT u.keyId, u.mobile, u.nickName, u.trueName, u.sex, u.photo, u.isReal");
		sql.append(", ui.idType, ui.idNo, ui.idiograph, ui.occupation, ui.age, ui.birthday, ui.constellation, ui.degree, ui.school, ui.company, ui.address");
		sql.append(" FROM " + User.tableName + " u");
		sql.append(" INNER JOIN " + UserInfo.tableName
				+ " ui ON ui.userId = u.keyId");

		StringBuffer where = new StringBuffer();
		if (StringUtils.isNotEmpty(keyword)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("(u.userName LIKE ? OR u.mobile LIKE ? OR u.nickName LIKE ? OR u.trueName LIKE ?)");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		}

		if (StringUtils.isNotEmpty(sex)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("u.sex = ?");
			params.add(sex);
		}

		if (StringUtils.isNotEmpty(idNo)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.idNo LIKE ?");
			params.add("%" + idNo + "%");
		}

		if (StringUtils.isNotEmpty(occupation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.occupation = ?");
			params.add(occupation);
		}

		if (StringUtils.isNotEmpty(minAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.age >= ?");
			params.add(Integer.parseInt(minAge));
		}

		if (StringUtils.isNotEmpty(maxAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.age <= ?");
			params.add(Integer.parseInt(maxAge));
		}

		if (StringUtils.isNotEmpty(beginDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.birthday >= ?");
			params.add(DateUtils.strToDate(beginDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.birthday <= ?");
			params.add(DateUtils.strToDate(endDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(constellation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.constellation = ?");
			params.add(constellation);
		}

		if (StringUtils.isNotEmpty(degree)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.degree = ?");
			params.add(degree);
		}

		if (StringUtils.isNotEmpty(school)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.school LIKE ?");
			params.add("%" + school + "%");
		}

		if (StringUtils.isNotEmpty(company)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.company LIKE ?");
			params.add("%" + company + "%");
		}

		if (StringUtils.isNotEmpty(address)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("ui.address LIKE ?");
			params.add("%" + address + "%");
		}
		sql.append(where);
		sql.append(" ORDER BY u.createDate DESC");

		SQLAndParam sqlAndParam = new SQLAndParam();
		sqlAndParam.setSql(sql.toString());
		sqlAndParam.setParamList(params);

		return sqlAndParam;
	}

	/**
	 * HQL语句和参数(Param为List)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	private SQLAndParam getHQLParamList(HttpServletRequest request) {
		String keyword = ParamUtils.getStringDefault(request, "keyword", "");
		String sex = ParamUtils.getStringDefault(request, "sex", "");
		String idNo = ParamUtils.getStringDefault(request, "idNo", "");
		String occupation = ParamUtils.getStringDefault(request, "occupation",
				"");
		String minAge = ParamUtils.getStringDefault(request, "minAge", "");
		String maxAge = ParamUtils.getStringDefault(request, "maxAge", "");
		String beginDate = ParamUtils
				.getStringDefault(request, "beginDate", "");
		String endDate = ParamUtils.getStringDefault(request, "endDate", "");
		String constellation = ParamUtils.getStringDefault(request,
				"constellation", "");
		String degree = ParamUtils.getStringDefault(request, "degree", "");
		String school = ParamUtils.getStringDefault(request, "school", "");
		String company = ParamUtils.getStringDefault(request, "company", "");
		String address = ParamUtils.getStringDefault(request, "address", "");

		StringBuffer sql = new StringBuffer();

		List<Object> params = new ArrayList<Object>();

		sql.append("FROM User");

		StringBuffer where = new StringBuffer();
		if (StringUtils.isNotEmpty(keyword)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("(userName LIKE ?0 OR mobile LIKE ?1 OR nickName LIKE ?2 OR trueName LIKE ?3)");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
			params.add("%" + keyword + "%");
		}

		if (StringUtils.isNotEmpty(sex)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("sex = ?" + Integer.valueOf(params.size()).toString());
			params.add(sex);
		}

		if (StringUtils.isNotEmpty(idNo)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.idNo LIKE ?"
					+ Integer.valueOf(params.size()).toString());
			params.add("%" + idNo + "%");
		}

		if (StringUtils.isNotEmpty(occupation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.occupation = ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(occupation);
		}

		if (StringUtils.isNotEmpty(minAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.age >= ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(Integer.parseInt(minAge));
		}

		if (StringUtils.isNotEmpty(maxAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.age <= ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(Integer.parseInt(maxAge));
		}

		if (StringUtils.isNotEmpty(beginDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.birthday >= ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(DateUtils.strToDate(beginDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.birthday <= ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(DateUtils.strToDate(endDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(constellation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.constellation = ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(constellation);
		}

		if (StringUtils.isNotEmpty(degree)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.degree = ?"
					+ Integer.valueOf(params.size()).toString());
			params.add(degree);
		}

		if (StringUtils.isNotEmpty(school)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.school LIKE ?"
					+ Integer.valueOf(params.size()).toString());
			params.add("%" + school + "%");
		}

		if (StringUtils.isNotEmpty(company)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.company LIKE ?"
					+ Integer.valueOf(params.size()).toString());
			params.add("%" + company + "%");
		}

		if (StringUtils.isNotEmpty(address)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.address LIKE ?"
					+ Integer.valueOf(params.size()).toString());
			params.add("%" + address + "%");
		}
		sql.append(where);

		SQLAndParam sqlAndParam = new SQLAndParam();
		sqlAndParam.setSql(sql.toString());
		sqlAndParam.setParamList(params);

		return sqlAndParam;
	}

	/**
	 * HQL语句和参数(Param为Map)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	private SQLAndParam getHQLParamMap(HttpServletRequest request) {
		String keyword = ParamUtils.getStringDefault(request, "keyword", "");
		String sex = ParamUtils.getStringDefault(request, "sex", "");
		String idNo = ParamUtils.getStringDefault(request, "idNo", "");
		String occupation = ParamUtils.getStringDefault(request, "occupation",
				"");
		String minAge = ParamUtils.getStringDefault(request, "minAge", "");
		String maxAge = ParamUtils.getStringDefault(request, "maxAge", "");
		String beginDate = ParamUtils
				.getStringDefault(request, "beginDate", "");
		String endDate = ParamUtils.getStringDefault(request, "endDate", "");
		String constellation = ParamUtils.getStringDefault(request,
				"constellation", "");
		String degree = ParamUtils.getStringDefault(request, "degree", "");
		String school = ParamUtils.getStringDefault(request, "school", "");
		String company = ParamUtils.getStringDefault(request, "company", "");
		String address = ParamUtils.getStringDefault(request, "address", "");

		StringBuffer sql = new StringBuffer();

		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("FROM User");

		StringBuffer where = new StringBuffer();
		if (StringUtils.isNotEmpty(keyword)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("(userName LIKE :keyword OR mobile LIKE :keyword OR nickName LIKE :keyword OR trueName LIKE :keyword)");
			params.put("keyword", "%" + keyword + "%");
		}

		if (StringUtils.isNotEmpty(sex)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("sex = :sex");
			params.put("sex", sex);
		}

		if (StringUtils.isNotEmpty(idNo)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.idNo LIKE :idNo");
			params.put("idNo", "%" + idNo + "%");
		}

		if (StringUtils.isNotEmpty(occupation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.occupation = :occupation");
			params.put("occupation", occupation);
		}

		if (StringUtils.isNotEmpty(minAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.age >= :minAge");
			params.put("minAge", Integer.parseInt(minAge));
		}

		if (StringUtils.isNotEmpty(maxAge)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.age <= :maxAge");
			params.put("maxAge", Integer.parseInt(maxAge));
		}

		if (StringUtils.isNotEmpty(beginDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.birthday >= :beginDate");
			params.put("beginDate",
					DateUtils.strToDate(beginDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.birthday <= :endDate");
			params.put("endDate", DateUtils.strToDate(endDate, "yyyy-MM-dd"));
		}

		if (StringUtils.isNotEmpty(constellation)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.constellation = :constellation");
			params.put("constellation", constellation);
		}

		if (StringUtils.isNotEmpty(degree)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.degree = :degree");
			params.put("degree", degree);
		}

		if (StringUtils.isNotEmpty(school)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.school LIKE :school");
			params.put("school", "%" + school + "%");
		}

		if (StringUtils.isNotEmpty(company)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.company LIKE :company");
			params.put("company", "%" + company + "%");
		}

		if (StringUtils.isNotEmpty(address)) {
			where.append(where.toString().equalsIgnoreCase("") ? " WHERE "
					: " AND ");
			where.append("userInfo.address LIKE :address");
			params.put("address", "%" + address + "%");
		}
		sql.append(where);

		SQLAndParam sqlAndParam = new SQLAndParam();
		sqlAndParam.setSql(sql.toString());
		sqlAndParam.setParamMap(params);

		return sqlAndParam;
	}
}