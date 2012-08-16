package com.emergentideas.location;

import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationContextTest {

	@Test
	public void testBasics() throws Exception {
		
		Integer i1 = 23;
		Integer i2 = 24;
		
		InvocationContext context = new InvocationContext();
		
		context.setFoundParameter(Integer.class, i1);
		context.setFoundParameter(Number.class, i2);
		
		assertEquals(i1, context.getFoundParameter(Integer.class));
		assertEquals(i2, context.getFoundParameter(Number.class));
		
		context.setFoundParameter("int", Integer.class, i1);
		context.setFoundParameter("int", Number.class, i2);
		
		assertEquals(i1, context.getFoundParameter("int", Integer.class));
		assertEquals(i2, context.getFoundParameter("int", Number.class));
		
		
	}
}
