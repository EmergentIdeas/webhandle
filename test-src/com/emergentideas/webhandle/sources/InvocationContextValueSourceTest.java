package com.emergentideas.webhandle.sources;

import org.junit.Test;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.sources.InvocationContextValueSource;

import static org.junit.Assert.*;

public class InvocationContextValueSourceTest {

	@Test
	public void testConstruction() {
		
		InvocationContextValueSource source = new InvocationContextValueSource();
		InvocationContext context = new InvocationContext();
		
		TestObj o1 = new TestObj();
		TestObj o2 = new TestObj();
		
		assertFalse(source.canGet(null, TestObj.class, context));
		context.setFoundParameter(TestObj.class, o1);
		assertTrue(source.canGet(null, TestObj.class, context));
		assertFalse(source.canGet("named", TestObj.class, context));
		
		context.setFoundParameter("named", TestObj.class, o2);
		assertTrue(source.canGet("named", TestObj.class, context));
		
		assertEquals(o1, source.get(null, TestObj.class, context));
		assertEquals(o2, source.get("named", TestObj.class, context));
	}
}
