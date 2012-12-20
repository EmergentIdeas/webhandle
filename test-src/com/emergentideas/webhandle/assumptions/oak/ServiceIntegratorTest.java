package com.emergentideas.webhandle.assumptions.oak;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj7;
import com.emergentideas.webhandle.TestObj8;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

public class ServiceIntegratorTest {

	@Test
	public void testIntegration() throws Exception {
		Location loc = new AppLocation();
		
		ServiceIntegrator integrator = new ServiceIntegrator();
		
		TestObj obj = new TestObj();
		
		integrator.integrate(null, loc, null, obj);
		
		WebAppLocation webApp = new WebAppLocation(loc);
		assertEquals(obj, webApp.getServiceByName("hello"));
		assertNull(webApp.getServiceByName("there"));
		
		assertEquals(obj, webApp.getServiceByType("there"));
		assertEquals(obj, webApp.getServiceByType("world"));
		assertNull(webApp.getServiceByType("hello"));
		
		HandleAnnotationHandlerInvestigator handler = new HandleAnnotationHandlerInvestigator();
		integrator.integrate(null, loc, null, handler);
		
		HandlerInvestigator inv2 = webApp.getServiceByType(HandlerInvestigator.class);
		assertEquals(handler, inv2);
		
		inv2 = webApp.getServiceByType(HandleAnnotationHandlerInvestigator.class);
		assertNull(inv2);
		
		TestObj7 to7 = new TestObj7();
		TestObj8 to8 = new TestObj8();
		
		integrator.integrate(null, loc, null, to7);
		integrator.integrate(null, loc, null, to8);
		
		assertEquals(to7, webApp.getServiceByName("testObj7"));
		assertNull(webApp.getServiceByName("testObj8"));
		assertEquals(to8, webApp.getServiceByName("to8"));
		assertEquals(to8, webApp.getServiceByType(TestObj7.class));
		
	}
	
	
	
	
}
