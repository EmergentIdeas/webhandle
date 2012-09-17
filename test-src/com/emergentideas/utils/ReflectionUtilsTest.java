package com.emergentideas.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj2;

import static com.emergentideas.utils.ReflectionUtils.*;

public class ReflectionUtilsTest {

	@Test
	public void testReflectionMethods() throws Exception {
		
		assertFalse(isArrayType("hello"));
		assertTrue(isArrayType(new String[0]));
		
		assertTrue(makeArrayFromObject("hello") instanceof String[]);
		
		assertTrue(getArrayStyle(String.class) == String[].class);
		assertTrue(getArrayStyle(String[].class) == String[].class);
		assertEquals(String.class, getNonArrayStyle(String[].class));
		assertEquals(String.class, getNonArrayStyle(String.class));
		
		assertFalse(isSetterMethod(TestObj.class.getMethod("test1", String.class, Integer[].class, List.class)));
		assertTrue(isSetterMethod(TestObj.class.getMethod("setChild2", TestObj.class)));
		
		assertEquals("child2", getPropertyName(TestObj.class.getMethod("setChild2", TestObj.class)));
		
		assertTrue(isReturnTypeVoid(getFirstMethod(TestObj.class, "setOrder")));
		assertFalse(isReturnTypeVoid(getFirstMethod(TestObj.class, "getOrder")));
	}
	
	@Test
	public void testFindAnnotation() throws Exception {
		TestObj obj = new TestObj();
		
		NoInject ni = getAnnotation(TestObj.class.getMethod("setB", String.class), NoInject.class);
		assertNotNull(ni);
		
		ni = getAnnotation(TestObj.class.getMethod("setA", String.class), NoInject.class);
		assertNull(ni);
		
		assertTrue(hasAnnotation(TestObj.class.getMethod("setB", String.class), NoInject.class));
		assertFalse(hasAnnotation(TestObj.class.getMethod("setA", String.class), NoInject.class));
	}
	
	@Test
	public void testPrimitive() throws Exception {
		assertFalse(isPrimitive(Integer.class));
		assertTrue(isPrimitive(Integer.TYPE));
		
		assertTrue(isPrimitive(TestObj.class.getMethod("getOrder", null).getReturnType()));
		
		assertEquals(0, getDefault(Integer.TYPE));
	}
	
	@Test
	public void testLoadResource() throws Exception {
		ClassLoader cl = getClass().getClassLoader();
		
		Enumeration<URL> urls = cl.getResources("com/emergentideas/*");
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			System.out.println(url.toString());
		}
	}
	
	@Test
	public void testGetSetter() throws Exception {
		TestObj2 obj2 = new TestObj2();
		Method m = getSetterMethod(obj2, "a");
		assertNotNull(m);
		assertEquals("setA", m.getName());
		
		m = getSetterMethod(obj2, "inner");
		assertNotNull(m);
		assertEquals("setInner", m.getName());
		
	}
		
}
