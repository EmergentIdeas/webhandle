package com.emergentideas.webhandle.db;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import com.emergentideas.webhandle.TestObj;

@Resource
public class MakeAJPAChange implements Runnable {

	@Resource
	protected EntityManager entityManager;
	
	@Override
	public void run() {
		entityManager.getTransaction().begin();

        TestObj obj = new TestObj("a", "b");
        obj.setId("" + System.currentTimeMillis());
        entityManager.persist(obj);
        
        entityManager.getTransaction().commit();

	}

}
