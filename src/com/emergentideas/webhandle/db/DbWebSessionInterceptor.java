package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.HandlingFail;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.PreResponse;
import com.emergentideas.webhandle.Wire;

public class DbWebSessionInterceptor {
	
	protected EntityManager entityManager;
	protected Logger log = SystemOutLogger.get(DbWebSessionInterceptor.class);
	
	@PreRequest
	public void startSession() {
		try {
			if(isReady() == false) {
				return;
			}
			entityManager.getTransaction().begin();
		}catch(Exception e) { log.error("Entity manager problem", e); }
	}
	
	@PreResponse
	public void commit() {
		try {
			if(isReady() == false) {
				return;
			}
			entityManager.getTransaction().commit();
			entityManager.close();
		}catch(Exception e) { log.error("Entity manager problem", e); }
	}
	
	@HandlingFail
	public void rollback() {
		try {
			if(isReady() == false) {
				return;
			}
			entityManager.getTransaction().rollback();
			entityManager.close();
		}catch(Exception e) { log.error("Entity manager problem", e); }
	}
	
	protected boolean isReady() {
		if(entityManager == null) {
			return false;
		}
		
		if(entityManager instanceof ProxiedThreadLocalEntityManager) {
			ProxiedThreadLocalEntityManager em = (ProxiedThreadLocalEntityManager)entityManager;
			return em.isReady();
		}
		
		return true;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
	

}
