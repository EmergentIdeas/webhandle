package com.emergentideas.webhandle.transformers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClearBooleansTransformerTest {
	
	@Test
	public void testClearBooleanTransformer() throws Exception {
		ClearBooleansValueTransformer cbvt = new ClearBooleansValueTransformer();
		
		BooleanSettersObj bso = new BooleanSettersObj();
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put("clear", new String[0]);
		properties.put("dontClear", new String[0]);
		properties.put("clearValue", false);
		
		cbvt.transform(null, properties, null, null, new Object[] {bso});
		
		assertFalse(bso.isMember1());
		assertFalse(bso.getMember3());
		assertTrue(bso.isMember4());
		assertTrue(bso.isMember2());
		
		bso.turnAllValues(true);
		
		properties.put("dontClear", new String[] { "member3" });
		cbvt.transform(null, properties, null, null, new Object[] {bso});
		
		assertFalse(bso.isMember1());
		assertTrue(bso.getMember3());
		assertTrue(bso.isMember4());
		assertTrue(bso.isMember2());
		
		properties.put("clear", new String[] { "member3" });
		properties.put("dontClear", new String[0]);
		bso.turnAllValues(true);
		cbvt.transform(null, properties, null, null, new Object[] {bso});
		assertTrue(bso.isMember1());
		assertFalse(bso.getMember3());
		assertTrue(bso.isMember4());
		assertTrue(bso.isMember2());
		
		bso.turnAllValues(false);
		properties.put("clearValue", true);
		cbvt.transform(null, properties, null, null, new Object[] {bso});
		assertFalse(bso.isMember1());
		assertTrue(bso.getMember3());
		assertFalse(bso.isMember4());
		assertFalse(bso.isMember2());
		
	}

}
