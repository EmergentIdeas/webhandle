package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.HandlingFail;
import com.emergentideas.webhandle.PostResponse;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.PreResponse;
import com.emergentideas.webhandle.Wire;

public class DbWebSessionInterceptor {
	
	public static final String REQUEST_TOUCH_ATTRIBUTE = "sessionTouchCount";
	
	protected EntityManager entityManager;
	protected Logger log = SystemOutLogger.get(DbWebSessionInterceptor.class);
	
	@PreRequest
	public void startSession(HttpServletRequest request) {
		try {
			if(isReady() == false) {
				return;
			}
			
			Integer i = (Integer)request.getAttribute(REQUEST_TOUCH_ATTRIBUTE);
			if(i == null) {
				request.setAttribute(REQUEST_TOUCH_ATTRIBUTE, 1);
				entityManager.getTransaction().begin();
			}
			else {
				request.setAttribute(REQUEST_TOUCH_ATTRIBUTE, i + 1);
			}
		}catch(Exception e) { 
			log.error("Entity manager problem, trying to cleanup", e);
			if(entityManager instanceof ProxiedThreadLocalEntityManager) {
				ProxiedThreadLocalEntityManager proxy = (ProxiedThreadLocalEntityManager)entityManager;
				proxy.wipeClean();
				
				try {
					entityManager.getTransaction().begin();
				}catch(Exception ex) { log.error("Entity manager problem even after cleanup attempt.", ex); }
			}
		}
	}
	
	@PreResponse
	public void commit() {
		if(isReady() == false) {
			return;
		}

		entityManager.getTransaction().commit();
		entityManager.getTransaction().begin();
	}
	
	@HandlingFail
	public void rollback(HttpServletRequest request) {
		try {
			if(isReady() == false) {
				return;
			}
			entityManager.getTransaction().rollback();
			entityManager.close();
			request.setAttribute(REQUEST_TOUCH_ATTRIBUTE, 0);
		}catch(Exception e) { log.error("Entity manager problem", e); }
	}
	
	@PostResponse
	public void afterRender(HttpServletRequest request) {
		try {
			if(isReady() == false) {
				return;
			}
			Integer i = (Integer)request.getAttribute(REQUEST_TOUCH_ATTRIBUTE);
			if(i != null) {
				if(i == 1) {
					entityManager.getTransaction().rollback();
					entityManager.close();
				}
				else if(i > 1){
					request.setAttribute(REQUEST_TOUCH_ATTRIBUTE, i - 1);
				}
			}
		}catch(Exception e) { 
			// This is okay since we don't really want anything to happen here 
		}
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
