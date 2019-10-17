package com.maven.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maven.common.page.Pager;
import com.maven.dao.IBaseDao;
import com.maven.service.IBaseService;

/**
 * 基础Service实现类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@Service
public class BaseService implements IBaseService {

	@Autowired
	private IBaseDao baseDao;

	@Cacheable(value = "baseCache", key = "#id")
	@Override
	public <T> T findById(Class<T> clazz, Serializable id) {
		return baseDao.findById(clazz, id);
	}

	@Override
	public <T> T findByCondition(Class<T> clazz, String hql,
			Map<String, Object> params) {
		return baseDao.findByCondition(clazz, hql, params);
	}

	@Override
	public <T> T findByCondition(Class<T> clazz, String hql, Object[] params) {
		return baseDao.findByCondition(clazz, hql, params);
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz, String hql,
			Map<String, Object> params) {
		return baseDao.findAll(clazz, hql, params);
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz, String hql, Object[] params) {
		return baseDao.findAll(clazz, hql, params);
	}

	@Override
	public <T> Pager findPage(Class<T> clazz, String hql,
			Map<String, Object> params, int page, int pageSize) {
		return baseDao.findPage(clazz, hql, params, page, pageSize);
	}

	@Override
	public <T> Pager findPage(Class<T> clazz, String hql, Object[] params,
			int page, int pageSize) {
		return baseDao.findPage(clazz, hql, params, page, pageSize);
	}

	@Override
	public <T> void save(T entity) {
		baseDao.save(entity);
	}

	@Override
	public <T> void update(T entity) {
		baseDao.update(entity);
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		baseDao.saveOrUpdate(entity);
	}

	@Override
	public <T> void delete(Class<T> clazz, Serializable id) {
		baseDao.delete(clazz, id);
	}

	@Override
	public Map<String, Object> findSql(String sql, Map<String, Object> params) {
		return baseDao.findSql(sql, params);
	}

	@Override
	public Map<String, Object> findSql(String sql, Object[] params) {
		return baseDao.findSql(sql, params);
	}

	@Override
	public List<Map<String, Object>> findAllSql(String sql,
			Map<String, Object> params) {
		return baseDao.findAllSql(sql, params);
	}

	@Override
	public List<Map<String, Object>> findAllSql(String sql, Object[] params) {
		return baseDao.findAllSql(sql, params);
	}

	@Override
	public Pager findPageSql(String sql, Map<String, Object> params, int page,
			int pageSize) {
		return baseDao.findPageSql(sql, params, page, pageSize);
	}

	@Override
	public Pager findPageSql(String sql, Object[] params, int page, int pageSize) {
		return baseDao.findPageSql(sql, params, page, pageSize);
	}

	@Override
	public void executeSql(String sql, Map<String, Object> params) {
		baseDao.executeSql(sql, params);
	}

	@Override
	public void executeSql(String sql, Object[] params) {
		baseDao.executeSql(sql, params);
	}
}