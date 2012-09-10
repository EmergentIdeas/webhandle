package com.emergentideas.webhandle.assumptions.oak;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.emergentideas.webhandle.WebAppLocation;

public class AppLoaderTest {

	@Test
	public void testDynamicClassDirectories() throws Exception {
		AppLoader appLoader = new AppLoader();
		appLoader.load(new File("appLoaderTest.conf"));
		
		WebAppLocation webApp = new WebAppLocation(appLoader.getLocation());
		ClassLoader cl = (ClassLoader)webApp.getServiceByName(AppLoader.CLASS_LOADER_NAME);
		Class c = cl.loadClass("com.emergentideas.webhandle.TestObjNoSource");
		
		assertNotNull(c);
		assertEquals("com.emergentideas.webhandle.TestObjNoSource", c.getName());
	}
}
