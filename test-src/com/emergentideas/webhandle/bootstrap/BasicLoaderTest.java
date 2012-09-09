package com.emergentideas.webhandle.bootstrap;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

public class BasicLoaderTest {

	@Test
	public void testFindDelegateLoader() throws Exception {
		
		
		
		
		
	}
	
	@Test
	public void testIncludedFiles() throws Exception {
		BasicLoader loader = new BasicLoader();
		AppLocation loc = new AppLocation();
		
		loader.load(loc, new FlatFileConfigurationParser().parse(StringUtils.getStreamFromClassPathLocation("com/emergentideas/webhandle/bootstrap/config2.conf")));
		
		
		WebAppLocation webApp = new WebAppLocation(loc);
		assertNotNull(webApp.getServiceByName("hello"));
		assertNull(webApp.getServiceByName("there"));
		
		assertNotNull(webApp.getServiceByType("there"));
		assertNotNull(webApp.getServiceByType("world"));
		assertNull(webApp.getServiceByType("hello"));
		
	}
	
	
}
