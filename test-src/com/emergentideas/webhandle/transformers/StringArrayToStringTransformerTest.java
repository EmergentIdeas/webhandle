package com.emergentideas.webhandle.transformers;

import org.junit.Test;

import com.emergentideas.webhandle.transformers.StringArrayToStringTransformer;

import static org.junit.Assert.*;

public class StringArrayToStringTransformerTest {

	@Test
	public void testTransformDate() {
		
		StringArrayToStringTransformer transformer = new StringArrayToStringTransformer();
		
		assertEquals("one", transformer.transform(null, null, null, null, new String[] { "one" }));
		assertEquals("one,two", transformer.transform(null, null, null, null, new String[] { "one", "two" }));
		assertEquals("one,two,three", transformer.transform(null, null, null, null, new String[] { "one", "two", "three" }));
	}
}
