package com.emergentideas.webhandle.assumptions.oak;

import static junit.framework.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj2;
import com.emergentideas.webhandle.WebAppLocation;

public class AutoWireIntegratorTest {

	@Test
	public void testMethodAutoWire() throws Exception {
		AppLocation location = new AppLocation();
		WebAppLocation webApp = new WebAppLocation(location);
		
		webApp.setServiceByName("a", "this is a");
		webApp.setServiceByName("b", "this is b");
		webApp.setServiceByName("c", new String[] { "hello", "world" });
		AutoWireIntegrator integrator = new AutoWireIntegrator();
		TestObj obj1 = new TestObj();
		
		integrator.integrate(null, location, null, obj1);
		
		assertEquals("this is a", obj1.getA());
		assertNull(obj1.getB());
		
		assertEquals("hello, world", StringUtils.join(obj1.getC(), ", "));
		
		TestObj2 obj2 = new TestObj2();
		webApp.setServiceByName("inner", obj1);
		
		integrator.integrate(null, location, null, obj2);
		assertEquals("this is a", obj2.getA());
		assertEquals("this is b", obj2.getB());
		assertEquals("hello", obj2.getC());
		assertEquals(obj1, obj2.getInner());
	}
}
