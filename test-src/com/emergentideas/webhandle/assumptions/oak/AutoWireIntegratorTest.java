package com.emergentideas.webhandle.assumptions.oak;

import static junit.framework.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj2;
import com.emergentideas.webhandle.TestObj8;
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
	
	@Test
	public void testFieldAutoWire() throws Exception {
		AppLocation location = new AppLocation();
		WebAppLocation webApp = new WebAppLocation(location);
		
		webApp.setServiceByName("private7", "private7");
		webApp.setServiceByName("protected7", "protected7");
		webApp.setServiceByName("package7", "package7");
		webApp.setServiceByName("public7", "public7");
		webApp.setServiceByName("private8", "private8");
		webApp.setServiceByName("protected8", "protected8");
		webApp.setServiceByName("protected8", "protected8");
		webApp.setServiceByName("package8", "package8");
		webApp.setServiceByName("public8", "public8");
		webApp.setServiceByName("notAResource", "notAResource");
		
		webApp.setServiceByName("npv8", "npv8");
		webApp.setServiceByName("npr8", "npr8");
		webApp.setServiceByName("npa8", "npa8");
		webApp.setServiceByName("npu8", "npu8");
		
		AutoWireIntegrator integrator = new AutoWireIntegrator();
		
		TestObj8 obj1 = new TestObj8();
		
		integrator.integrate(null, location, null, obj1);
		
		
		assertEquals("private7", obj1.getPrivate7());
		assertEquals("protected7", obj1.getProtected7());
		assertEquals("package7", obj1.getPackage7());
		assertEquals("public7", obj1.getPublic7());
		assertEquals("private8", obj1.getPrivate8());
		assertEquals("protected8", obj1.getProtected8());
		assertEquals("package8", obj1.getPackage8());
		assertEquals("public8", obj1.getPublic8());
		assertEquals("not set", obj1.getNotAResource());
		
		assertEquals("npv8", obj1.getNamedPrivate8());
		assertEquals("npr8", obj1.getNamedProtected8());
		assertEquals("npa8", obj1.getNamedPackage8());
		assertEquals("npu8", obj1.getNamedPublic8());
		
	}

}
