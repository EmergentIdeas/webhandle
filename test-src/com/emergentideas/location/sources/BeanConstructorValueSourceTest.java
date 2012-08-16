package com.emergentideas.location.sources;

import org.junit.Test;

import com.emergentideas.location.Location;
import com.emergentideas.location.TestObj;

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
