package com.emergentideas.utils;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj5;

public class BeanInfoUtilsTest {

	@Test
	public void testGetNonIdPropertyNames() {
		List<String> names;
		
		names = BeanInfoUtils.determineNonIdNonObjectPropertyNames(TestObj.class);
		
		assertEquals(3, names.size());
		assertTrue(names.contains("a"));
		assertTrue(names.contains("b"));
		assertTrue(names.contains("order"));
		
		
		names = BeanInfoUtils.determineNonIdNonObjectPropertyNames(TestObj5.class);
		
		assertEquals(4, names.size());
		assertTrue(names.contains("d"));
		assertTrue(names.contains("e"));
		assertTrue(names.contains("f"));
		assertTrue(names.contains("g"));

	}
	
	@Test
	public void testGetNonIdPropertyNamesWithGetters() {
		
		List<String> names = BeanInfoUtils.determineNonIdNonObjectPropertyNamesWithGetters(TestObj.class);
		
		assertEquals(3, names.size());
		assertTrue(names.contains("a"));
		assertTrue(names.contains("b"));
		assertTrue(names.contains("order"));
		
		names = BeanInfoUtils.determineNonIdNonObjectPropertyNamesWithGetters(TestObj5.class);
		
		assertEquals(0, names.size());
	}
	
	@Test
	public void testFormatPropertyName() {
		assertEquals("Hello", BeanInfoUtils.formatCamelCasePropertyName("hello"));
		assertEquals("Hello There", BeanInfoUtils.formatCamelCasePropertyName("helloThere"));
	}
	
	@Test
	public void testDetermineIdProperty() {
		assertEquals("id", BeanInfoUtils.determineIdPropertyNameWithGetter(TestObj.class));
	}
}
