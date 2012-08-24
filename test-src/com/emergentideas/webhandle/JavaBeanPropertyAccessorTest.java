package com.emergentideas.webhandle;

import org.junit.Test;

import com.emergentideas.webhandle.JavaBeanPropertyAccessor;

import static org.junit.Assert.*;

public class JavaBeanPropertyAccessorTest {
	
	@Test
	public void testBasics() throws Exception {
		
		TestObj o1 = new TestObj();
		
		o1.setA("hello");
		o1.setB("there");
		
		JavaBeanPropertyAccessor accessor = new JavaBeanPropertyAccessor();
		assertTrue(accessor.canAccess(o1, "a"));
		assertTrue(accessor.canAccess(o1, "b"));
		assertFalse(accessor.canAccess(o1, "z"));
		
		assertEquals("hello", accessor.get(o1, "a"));
		assertEquals("there", accessor.get(o1, "b"));
	}
}
