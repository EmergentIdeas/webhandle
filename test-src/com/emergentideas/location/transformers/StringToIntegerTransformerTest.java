package com.emergentideas.location.transformers;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringToIntegerTransformerTest {

	@Test
	public void testIntegerConvertsion() {
		StringToIntegerTransformer transformer = new StringToIntegerTransformer();
		
		Integer[] ints = transformer.transform(null, null, "12", "15", "16.6");
		assertEquals(3, ints.length);
		
		assertEquals((Integer)12, ints[0]);
		assertEquals((Integer)15, ints[1]);
		assertEquals((Integer)16, ints[2]);
		
	}
}
