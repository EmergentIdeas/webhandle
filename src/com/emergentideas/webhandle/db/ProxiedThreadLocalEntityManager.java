package com.emergentideas.webhandle.db;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Type;

@Type({"javax.persistence.EntityManager", "com.emergentideas.webhandle.db.ProxiedThreadLocalEntityManager"})
public class ProxiedThreadLocalEntityManager implements EntityManager {

	protected EntityManagerFactory factory;
	protected ThreadLocal<EntityManager> currentManager = new ThreadLocal<EntityManager>();
	
	// The length of time in ms that we should spend trying to obtain a good connection
	protected int testForGoodConnectionTimeout = 3000;
	
	// An SQL to test that the connection is active
	protected String testConnectionSQL = "select 1";
	
	protected Logger log = SystemOutLogger.get(ProxiedThreadLocalEntityManager.class);
	
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
	
	

	public <T> T find(Class<T> entityClass, Object primaryKey,
			Map<String, Object> properties) {
		return em().find(entityClass, primaryKey, properties);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode) {
		return em().find(entityClass, primaryKey, lockMode);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode, Map<String, Object> properties) {
		return em().find(entityClass, primaryKey, lockMode, properties);
	}

	public void lock(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		em().lock(entity, lockMode, properties);
	}

	public void refresh(Object entity, Map<String, Object> properties) {
		em().refresh(entity, properties);
	}

	public void refresh(Object entity, LockModeType lockMode) {
		em().refresh(entity, lockMode);
	}

	public void refresh(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		em().refresh(entity, lockMode, properties);
	}

	public void detach(Object entity) {
		em().detach(entity);
	}

	public LockModeType getLockMode(Object entity) {
		return em().getLockMode(entity);
	}

	public void setProperty(String propertyName, Object value) {
		em().setProperty(propertyName, value);
	}

	public Map<String, Object> getProperties() {
		return em().getProperties();
	}

	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return em().createQuery(criteriaQuery);
	}

	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return em().createQuery(qlString, resultClass);
	}

	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return em().createNamedQuery(name, resultClass);
	}

	public <T> T unwrap(Class<T> cls) {
		return em().unwrap(cls);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return em().getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return em().getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return em().getMetamodel();
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}
	
	public int getTestForGoodConnectionTimeout() {
		return testForGoodConnectionTimeout;
	}

	public void setTestForGoodConnectionTimeout(int testForGoodConnectionTimeout) {
		this.testForGoodConnectionTimeout = testForGoodConnectionTimeout;
	}

	public String getTestConnectionSQL() {
		return testConnectionSQL;
	}

	public void setTestConnectionSQL(String testConnectionSQL) {
		this.testConnectionSQL = testConnectionSQL;
	}

	public boolean isReady() {
		if(factory == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * In case we get in some weird state, this will wipe out the entity manager for this thread
	 * so we can acquire a new one.
	 */
	public void wipeClean() {
		if(currentManager.get() != null) {
			currentManager.remove();
		}
	}

	protected EntityManager em() {
		if(factory == null) {
			throw new UnsupportedOperationException();
		}
		if(currentManager.get() == null) {
			long start = System.currentTimeMillis();
			do {
				EntityManager em = factory.createEntityManager();
				if(isConnectionAlive(em)) {
					currentManager.set(em);
					break;
				}
			}
			while((System.currentTimeMillis() - start) < testForGoodConnectionTimeout);
		}
		return currentManager.get();
	}
	
	protected boolean isConnectionAlive(EntityManager em) {
		try {
			em.createNativeQuery(testConnectionSQL).getResultList();
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}
}
