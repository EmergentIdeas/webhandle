package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;

import com.emergentideas.webhandle.HandlingFail;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.PreResponse;
import com.emergentideas.webhandle.Wire;

public class DbWebSessionInterceptor {
	
	EntityManager entityManager;
	
	@PreRequest
	public void startSession() {
		entityManager.getTransaction().begin();
	}
	
	@PreResponse
	public void commit() {
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	@HandlingFail
	public void rollback() {
		entityManager.getTransaction().rollback();
		entityManager.close();
	}
	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
	

}
