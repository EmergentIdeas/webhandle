package com.emergentideas.webhandle.db;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.assumptions.oak.Constants;

public class ListenerSeparationTests {
	
	@Test
	public void testDBLoad() throws Exception {
		
		AppLoader loader = new AppLoader();
		loader.load(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/db/jpaWatcherOne.conf"), new File("").getAbsoluteFile());

		WebAppLocation webApp = new WebAppLocation(loader.getLocation());
        
        ClassLoader cloader = (ClassLoader)webApp.getServiceByName(Constants.CLASS_LOADER_NAME);
        
        Thread.currentThread().setContextClassLoader(cloader);
        
        Runnable r = (Runnable)webApp.getServiceByName("makeAJPAChange");
        r.run();
	}

}
