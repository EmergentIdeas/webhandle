package com.emergentideas.location;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapPropertyAccessorTest {
	
	@Test
	public void testBasics() throws Exception {
		
		Map<String, String> o1 = new HashMap<String, String>();
		
		o1.put("a", "hello");
		o1.put("b", "there");
		
		MapPropertyAccessor accessor = new MapPropertyAccessor();
		assertTrue(accessor.canAccess(o1, "a"));
		assertTrue(accessor.canAccess(o1, "b"));
		assertFalse(accessor.canAccess(o1, "z"));
		
		assertEquals("hello", accessor.get(o1, "a"));
		assertEquals("there", accessor.get(o1, "b"));
	}
}
