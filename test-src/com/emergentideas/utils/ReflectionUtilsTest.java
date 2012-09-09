package com.emergentideas.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.TestObj;
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
		
}
