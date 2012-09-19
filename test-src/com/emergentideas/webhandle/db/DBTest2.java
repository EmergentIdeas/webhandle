package com.emergentideas.webhandle.db;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;

public class DBTest2 {
	
	@Test
	public void testDBLoad() throws Exception {
		
		AppLoader loader = new AppLoader();
		loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/db/dbTest2.conf"), new File("").getAbsoluteFile());

		WebAppLocation webApp = new WebAppLocation(loader.getLocation());
		EntityManager manager = webApp.getServiceByType(EntityManager.class);
        
        
        manager.getTransaction().begin();

        TestObj obj = new TestObj("a", "b");
        obj.setId("" + System.currentTimeMillis());
        manager.persist(obj);
        
        manager.getTransaction().commit();
       
	}

}
