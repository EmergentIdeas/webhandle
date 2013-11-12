package com.emergentideas.webhandle.transformers;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringToEnumTransformerTest {

	@Test
	public void testTransformDate() {
		StringToEnumTransformer transformer = new StringToEnumTransformer();
		
		assertEquals(TestEnum.ONE, transformer.transform(null, null, TestEnum.class, "en", "ONE")[0]);
		assertEquals(TestEnum.TWO, transformer.transform(null, null, TestEnum.class, "en", "TWO")[0]);
		assertEquals(TestEnum.THREE, transformer.transform(null, null, TestEnum.class, "en", "THREE")[0]);
		assertNull(transformer.transform(null, null, TestEnum.class, "en", "ZERO"));
		
		assertEquals(TestEnum[].class, transformer.transform(null, null, TestEnum.class, "en", "ONE").getClass());
		
		assertEquals(TestEnum.ONE, transformer.transform(null, null, TestEnum[].class, "en", "ONE")[0]);
		assertEquals(TestEnum[].class, transformer.transform(null, null, TestEnum[].class, "en", "ONE").getClass());
		assertNull(transformer.transform(null, null, TestEnum.class, "en", "ZERO"));

	}
	
	public enum TestEnum { ONE, TWO, THREE }
}
