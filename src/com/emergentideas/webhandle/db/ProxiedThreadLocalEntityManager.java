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
	
	enum TransactionState { UNTOUCHED, BEGIN_CALLED, BEGUN }

	protected EntityManagerFactory factory;
	protected ThreadLocal<EntityManager> currentManager = new ThreadLocal<EntityManager>();
	protected ThreadLocal<InnerTransaction> currentTransaction = new ThreadLocal<ProxiedThreadLocalEntityManager.InnerTransaction>();
	
	// The length of time in ms that we should spend trying to obtain a good connection
	protected int testForGoodConnectionTimeout = 3000;
	
	// An SQL to test that the connection is active
	protected String testConnectionSQL = "select 1";
	
	protected Logger log = SystemOutLogger.get(ProxiedThreadLocalEntityManager.class);
	
	public void persist(Object entity) {
		beginIfWarrented();
		em().persist(entity);
	}

	public <T> T merge(T entity) {
		beginIfWarrented();
		return em().merge(entity);
	}

	public void remove(Object entity) {
		beginIfWarrented();
		em().remove(entity);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey) {
		beginIfWarrented();
		return em().find(entityClass, primaryKey);
	}

	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		beginIfWarrented();
		return em().getReference(entityClass, primaryKey);
	}

	public void flush() {
		beginIfWarrented();
		em().flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		beginIfWarrented();
		em().setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		beginIfWarrented();
		return em().getFlushMode();
	}

	public void lock(Object entity, LockModeType lockMode) {
		beginIfWarrented();
		em().lock(entity, lockMode);
	}

	public void refresh(Object entity) {
		beginIfWarrented();
		em().refresh(entity);
	}

	public void clear() {
		beginIfWarrented();
		em().clear();
	}

	public boolean contains(Object entity) {
		beginIfWarrented();
		return em().contains(entity);
	}

	public Query createQuery(String qlString) {
		beginIfWarrented();
		return em().createQuery(qlString);
	}

	public Query createNamedQuery(String name) {
		beginIfWarrented();
		return em().createNamedQuery(name);
	}

	public Query createNativeQuery(String sqlString) {
		beginIfWarrented();
		return em().createNativeQuery(sqlString);
	}

	public Query createNativeQuery(String sqlString, Class resultClass) {
		beginIfWarrented();
		return em().createNativeQuery(sqlString, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		beginIfWarrented();
		return em().createNativeQuery(sqlString, resultSetMapping);
	}

	public void joinTransaction() {
		beginIfWarrented();
		em().joinTransaction();

	}

	public Object getDelegate() {
		beginIfWarrented();
		return em().getDelegate();
	}

	public void close() {
		if(currentManager.get() != null) {
			currentManager.get().close();
		}
		currentManager.remove();
		currentTransaction.remove();
	}

	public boolean isOpen() {
		beginIfWarrented();
		return em().isOpen();
	}

	public EntityTransaction getTransaction() {
		if(currentTransaction.get() == null) {
			currentTransaction.set(new InnerTransaction(em().getTransaction()));
		}
		return currentTransaction.get();
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,
			Map<String, Object> properties) {
		beginIfWarrented();
		return em().find(entityClass, primaryKey, properties);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode) {
		beginIfWarrented();
		return em().find(entityClass, primaryKey, lockMode);
	}

	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode, Map<String, Object> properties) {
		beginIfWarrented();
		return em().find(entityClass, primaryKey, lockMode, properties);
	}

	public void lock(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		beginIfWarrented();
		em().lock(entity, lockMode, properties);
	}

	public void refresh(Object entity, Map<String, Object> properties) {
		beginIfWarrented();
		em().refresh(entity, properties);
	}

	public void refresh(Object entity, LockModeType lockMode) {
		beginIfWarrented();
		em().refresh(entity, lockMode);
	}

	public void refresh(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		beginIfWarrented();
		em().refresh(entity, lockMode, properties);
	}

	public void detach(Object entity) {
		beginIfWarrented();
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
		beginIfWarrented();
		return em().createQuery(criteriaQuery);
	}

	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		beginIfWarrented();
		return em().createQuery(qlString, resultClass);
	}

	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		beginIfWarrented();
		return em().createNamedQuery(name, resultClass);
	}

	public <T> T unwrap(Class<T> cls) {
		beginIfWarrented();
		return em().unwrap(cls);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		beginIfWarrented();
		return em().getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		beginIfWarrented();
		return em().getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		beginIfWarrented();
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
		if(currentTransaction.get() != null) {
			currentTransaction.remove();
		}
	}

	protected EntityManager em() {
		return em(false);
	}
	protected EntityManager em(boolean checkConnection) {
		if(factory == null) {
			throw new UnsupportedOperationException();
		}
		if(currentManager.get() == null) {
			long start = System.currentTimeMillis();
			do {
				EntityManager em = factory.createEntityManager();
				if((!checkConnection) || isConnectionAlive(em)) {
					currentManager.set(em);
					currentTransaction.remove();
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
	
	protected void beginIfWarrented() {
		InnerTransaction trans = currentTransaction.get();
		if(trans != null) {
			if(trans.getTransactionState() == TransactionState.BEGIN_CALLED) {
				trans.actualBegin();
			}
		}
	}
	
	class InnerTransaction implements EntityTransaction {

		protected EntityTransaction inner;
		protected TransactionState transactionState = TransactionState.UNTOUCHED;
		
		public InnerTransaction(EntityTransaction inner) {
			this.inner = inner;
		}
		
		public void begin() {
			transactionState = TransactionState.BEGIN_CALLED;
		}
		
		public void actualBegin() {
			if(transactionState != TransactionState.BEGUN) {
				try {
					inner.begin();
				}
				catch(Exception e) {
					// it's possible our connection has timed-out. Let's see if we can grab a new entitymanager
					// and try again.
					wipeClean();
					inner = em(true).getTransaction();
					inner.begin();
				}
				transactionState = TransactionState.BEGUN;
			}
		}

		public void commit() {
			if(transactionState == TransactionState.BEGUN) {
				inner.commit();
			}
		}

		public void rollback() {
			if(transactionState == TransactionState.BEGUN) {
				inner.rollback();
			}
		}

		public void setRollbackOnly() {
			inner.setRollbackOnly();
		}

		public boolean getRollbackOnly() {
			return inner.getRollbackOnly();
		}

		public boolean isActive() {
			return inner.isActive();
		}

		public EntityTransaction getInner() {
			return inner;
		}

		public void setInner(EntityTransaction inner) {
			this.inner = inner;
		}

		public TransactionState getTransactionState() {
			return transactionState;
		}

		public void setTransactionState(TransactionState transactionState) {
			this.transactionState = transactionState;
		}
		
		
	}
}
