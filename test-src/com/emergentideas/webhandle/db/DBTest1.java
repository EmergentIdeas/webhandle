package com.emergentideas.webhandle.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.emergentideas.webhandle.TestObj;

public class DBTest1 {
	
	@Test
	public void testDBLoad() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test1");

        EntityManager manager = factory.createEntityManager();

        
        
        manager.getTransaction().begin();

        TestObj obj = new TestObj("a", "b");
        obj.setId("" + System.currentTimeMillis());
        manager.persist(obj);
        
        manager.getTransaction().commit();
       
	}

}
