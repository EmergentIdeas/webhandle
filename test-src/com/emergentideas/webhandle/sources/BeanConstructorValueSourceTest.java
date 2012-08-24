package com.emergentideas.webhandle.sources;

import org.junit.Test;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.sources.BeanConstructorValueSource;

import static org.junit.Assert.*;

public class BeanConstructorValueSourceTest {

	@Test
	public void testConstruction() {
		
		BeanConstructorValueSource source = new BeanConstructorValueSource();
		assertTrue(source.canGet(null, TestObj.class, null));
		assertNotNull(source.get(null, TestObj.class, null));
		assertFalse(source.canGet(null, Location.class, null));
		assertNull(source.get(null, Location.class, null));
	}
}
