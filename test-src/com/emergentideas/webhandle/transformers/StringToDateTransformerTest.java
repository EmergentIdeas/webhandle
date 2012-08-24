package com.emergentideas.webhandle.transformers;

import java.util.Date;

import org.junit.Test;

import com.emergentideas.webhandle.transformers.StringToDateTransformer;

import static org.junit.Assert.*;

public class StringToDateTransformerTest {

	@Test
	public void testTransformDate() {
		Date d1 = new Date();
		
		StringToDateTransformer transformer = new StringToDateTransformer();
		
		assertTrue(d1.after(transformer.transform(null, null, null, null, "2010.01.16")[0]));
	}
}
