package com.maven.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.maven.common.page.Pager;

/**
 * 基础Dao接口
 * 
 * @author chenjian
 * @createDate 2018-12-28
 * @param <T>
 * @param <PK>
 */
public interface IBaseDao {

	/**
	 * 根据id获取信息
	 * 
	 * @param id
	 *            实体类主键编号
	 * @return
	 */
	<T> T findById(Class<T> clazz, Serializable id);

	/**
	 * 根据条件获取信息
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param param
	 *            条件参数
	 * @return
	 */
	<T> T findByCondition(Class<T> clazz, String hql, Map<String, Object> param);

	/**
	 * 根据条件获取信息
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param param
	 *            条件参数
	 * @return
	 */
	<T> T findByCondition(Class<T> clazz, String hql, Object[] param);

	/**
	 * 获取所有信息
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, String hql, Map<String, Object> params);

	/**
	 * 获取所有信息
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	<T> List<T> findAll(Class<T> clazz, String hql, Object[] params);

	/**
	 * 获取所有信息(分页)
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            条件参数
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	<T> Pager findPage(Class<T> clazz, String hql, Map<String, Object> params,
			int page, int pageSize);

	/**
	 * 获取所有信息(分页)
	 * 
	 * @param clazz
	 *            实体类
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            条件参数
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	<T> Pager findPage(Class<T> clazz, String hql, Object[] params, int page,
			int pageSize);

	/**
	 * 保存信息
	 * 
	 * @param entity
	 *            实体类
	 */
	<T> void save(T entity);

	/**
	 * 修改信息
	 * 
	 * @param entity
	 *            实体类
	 */
	<T> void update(T entity);

	/**
	 * 保存或修改信息
	 * 
	 * @param entity
	 *            实体类
	 */
	<T> void saveOrUpdate(T entity);

	/**
	 * 根据id删除信息
	 * 
	 * @param id
	 *            实体类主键编号
	 */
	<T> void delete(Class<T> clazz, Serializable id);

	/**
	 * 根据条件获取信息(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	Map<String, Object> findSql(String sql, Map<String, Object> params);

	/**
	 * 根据条件获取信息(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	Map<String, Object> findSql(String sql, Object[] params);

	/**
	 * 获取所有信息(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	List<Map<String, Object>> findAllSql(String sql, Map<String, Object> params);

	/**
	 * 获取所有信息(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @return
	 */
	List<Map<String, Object>> findAllSql(String sql, Object[] params);

	/**
	 * 获取所有信息分页(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	Pager findPageSql(String sql, Map<String, Object> params, int page,
			int pageSize);

	/**
	 * 获取所有信息分页(原生SQL)
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            条件参数
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	Pager findPageSql(String sql, Object[] params, int page, int pageSize);
}