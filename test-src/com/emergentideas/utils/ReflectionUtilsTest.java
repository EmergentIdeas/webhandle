package com.emergentideas.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj2;
import com.emergentideas.webhandle.TestObj5;
import com.emergentideas.webhandle.TestObjectParameterizedExtended;

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
	
	@Test
	public void testGetIdSetter() throws Exception {
		Method m = getIdSetterMethod(TestObj2.class);
		assertNull(m);
		m = getIdSetterMethod(TestObj.class);
		assertEquals("setId", m.getName());
	}
	
	@Test
	public void testContains() throws Exception {
		
		String[] list = new String[] { "one", null, "two" };
		
		assertTrue(contains(list, "one"));
		assertTrue(contains(list, null));
		assertTrue(contains(list, "two"));
		
		assertFalse(contains(list, "three"));
		
	}

	@Test
	public void testClassDistance() throws Exception {
		assertEquals((Integer)1, ReflectionUtils.findClassDistance(List.class, ArrayList.class));
		assertEquals((Integer)2, ReflectionUtils.findClassDistance(List.class, new ArrayList() {}.getClass()));
	}
	
	@Test
	public void testGetIdType() throws Exception {
		Class c = ReflectionUtils.determineIdClass(TestObj.class);
		assertEquals(String.class, c);
		
		c = ReflectionUtils.determineIdClass(TestObj2.class);
		assertEquals(Integer.class, c);
	}
	
	@Test
	public void testGetParameterizedArgumentType() throws Exception {
		TestObj5 obj = new TestObj5();
		Method method = getFirstMethod(obj.getClass(), "plus2");
		Type parameterType = method.getGenericParameterTypes()[0];
		
		Class c = determineParameterizedArgumentType(parameterType, obj);
		
		assertEquals(Integer.class, c);

	}
	
	@Test
	public void testFirstMethod() throws Exception {
		Method m = getFirstMethod(TestObjectParameterizedExtended.class, "method1");
		assertEquals(String.class, m.getGenericParameterTypes()[0]);
	}
}
