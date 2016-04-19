package com.woniu.base.service;

import com.woniu.base.db.DB;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 
 * @param <T>
 *            实体类型
 * @param <P>
 *            主键类型
 */
public abstract class EntityService<T, P> {
	protected final Class<T> klass;

	@Inject
	protected DB db;

	public EntityService(Class<T> klass) {
		this.klass = klass;
	}

	public T find(P id) {
		T entity = db.find(klass, id);
		afterFind(entity);
		return entity;
	}

	protected void afterFind(T entity) {
	}

	@Transactional
	public void create(T entity) {
		beforeCreate(entity);
		db.insert(entity);
		afterCreate(entity);
	}

	protected void beforeCreate(T entity) {
	}
	
	protected void afterCreate(T entity) {
	}

	@Transactional
	public void update(T entity) {
		beforeUpdate(entity);
		db.update(entity);
		afterUpdate(entity);
	}

	protected void beforeUpdate(T entity) {
	}
	
	protected void afterUpdate(T entity) {
	}

	@Transactional
	public T delete(P id) {
		T entity = find(id);
		return destroy(entity);
	}
	
	@Transactional
	public T destroy(T entity) {
		beforeDestroy(entity);
		db.delete(entity);
		afterDestroy(entity);
		return entity;
	}
	
	protected void beforeDestroy(T entity) {
	}
	
	protected void afterDestroy(T entity) {
	}

}
