package com.maven.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.maven.common.page.Pager;
import com.maven.dao.IBaseDao;

/**
 * 基础Dao实现类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
@Repository
public class BaseDao extends HibernateDaoSupport implements IBaseDao {

	@Autowired
	private void mySessionFactory(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	private Session getSession() {
		return this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
	}

	@Override
	public <T> T findById(Class<T> clazz, Serializable id) {
		return this.getHibernateTemplate().get(clazz, id);
	}

	@Override
	public <T> T findByCondition(Class<T> clazz, String hql,
			Map<String, Object> param) {
		return this.getHibernateTemplate().execute(new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql, clazz);
				query.setProperties(param);
				return null;
			}
		});
	}

	@Override
	public <T> T findByCondition(Class<T> clazz, String hql, Object[] param) {
		return this.getHibernateTemplate().execute(new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql, clazz);
				query.setProperties(param);
				return null;
			}
		});
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz, String hql,
			Map<String, Object> params) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List<T>>() {
					@Override
					public List<T> doInHibernate(Session session)
							throws HibernateException {
						Query query = (Query) session.createQuery(hql, clazz);
						query.setProperties(params);
						return query.list();
					}
				});
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz, String hql, Object[] params) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List<T>>() {

					@Override
					public List<T> doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql, clazz);
						query.setProperties(params);
						return query.list();
					}
				});
	}

	@Override
	public <T> Pager findPage(Class<T> clazz, String hql,
			Map<String, Object> params, int page, int pageSize) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<Pager>() {

					@Override
					public Pager doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql, clazz);
						query.setProperties(params);
						int count = query.list().size();

						Pager pager = new Pager();
						pager.setCurrentPage(page);
						pager.setPageSize(pageSize);
						pager.setRowsTotal(count);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						pager.setList(query.list());
						return pager;
					}
				});
	}

	@Override
	public <T> Pager findPage(Class<T> clazz, String hql, Object[] params,
			int page, int pageSize) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<Pager>() {

					@Override
					public Pager doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql, clazz);
						query.setProperties(params);
						int count = query.list().size();

						Pager pager = new Pager();
						pager.setCurrentPage(page);
						pager.setPageSize(pageSize);
						pager.setRowsTotal(count);
						query.setFirstResult((page - 1) * pageSize);
						query.setMaxResults(pageSize);
						pager.setList(query.list());
						return pager;
					}
				});
	}

	@Override
	public <T> void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}

	@Override
	public <T> void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	@Override
	public <T> void delete(Class<T> clazz, Serializable id) {
		this.getHibernateTemplate().delete(findById(clazz, id));
	}

	@Override
	public Map<String, Object> findSql(String sql, Map<String, Object> params) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		List<Map<String, Object>> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public Map<String, Object> findSql(String sql, Object[] params) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		List<Map<String, Object>> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public List<Map<String, Object>> findAllSql(String sql,
			Map<String, Object> params) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findAllSql(String sql, Object[] params) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.list();
	}

	@Override
	public Pager findPageSql(String sql, Map<String, Object> params, int page,
			int pageSize) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		int count = query.list().size();

		Pager pager = new Pager();
		pager.setCurrentPage(page);
		pager.setPageSize(pageSize);
		pager.setRowsTotal(count);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		pager.setList(query.list());
		return pager;
	}

	@Override
	public Pager findPageSql(String sql, Object[] params, int page, int pageSize) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		int count = query.list().size();

		Pager pager = new Pager();
		pager.setCurrentPage(page);
		pager.setPageSize(pageSize);
		pager.setRowsTotal(count);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		pager.setList(query.list());
		return pager;
	}
}