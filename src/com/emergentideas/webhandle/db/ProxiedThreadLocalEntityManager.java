package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.emergentideas.webhandle.Type;

@Type({"javax.persistence.EntityManager", "com.emergentideas.webhandle.db.ProxiedThreadLocalEntityManager"})
public class ProxiedThreadLocalEntityManager implements EntityManager {

	protected EntityManagerFactory factory;
	protected ThreadLocal<EntityManager> currentManager = new ThreadLocal<EntityManager>();
	
	public void persist(Object entity) {
		em().persist(entity);
	}

	public <T> T merge(T entity) {
		return em().merge(entity);
	}

	public void remove(Object entity) {
		em().remove(entity);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return em().find(entityClass, primaryKey);
	}

	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return em().getReference(entityClass, primaryKey);
	}

	public void flush() {
		em().flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		em().setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return em().getFlushMode();
	}

	public void lock(Object entity, LockModeType lockMode) {
		em().lock(entity, lockMode);
	}

	public void refresh(Object entity) {
		em().refresh(entity);
	}

	public void clear() {
		em().clear();
	}

	public boolean contains(Object entity) {
		return em().contains(entity);
	}

	public Query createQuery(String qlString) {
		return em().createQuery(qlString);
	}

	public Query createNamedQuery(String name) {
		return em().createNamedQuery(name);
	}

	public Query createNativeQuery(String sqlString) {
		return em().createNativeQuery(sqlString);
	}

	public Query createNativeQuery(String sqlString, Class resultClass) {
		return em().createNativeQuery(sqlString, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return em().createNativeQuery(sqlString, resultSetMapping);
	}

	public void joinTransaction() {
		em().joinTransaction();

	}

	public Object getDelegate() {
		return em().getDelegate();
	}

	public void close() {
		if(currentManager.get() != null) {
			currentManager.get().close();
		}
		currentManager.remove();
	}

	public boolean isOpen() {
		return em().isOpen();
	}

	public EntityTransaction getTransaction() {
		return em().getTransaction();
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}

	protected EntityManager em() {
		if(factory == null) {
			throw new UnsupportedOperationException();
		}
		if(currentManager.get() == null) {
			currentManager.set(factory.createEntityManager());
		}
		return currentManager.get();
	}
}
