package com.emergentideas.location.transformers;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayToStringTransformerTest {

	@Test
	public void testTransformDate() {
		
		StringArrayToStringTransformer transformer = new StringArrayToStringTransformer();
		
		assertEquals("one", transformer.transform(null, null, null, new String[] { "one" }));
		assertEquals("one,two", transformer.transform(null, null, null, new String[] { "one", "two" }));
		assertEquals("one,two,three", transformer.transform(null, null, null, new String[] { "one", "two", "three" }));
	}
}
