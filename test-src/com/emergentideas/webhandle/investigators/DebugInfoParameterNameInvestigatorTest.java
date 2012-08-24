package com.emergentideas.webhandle.investigators;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.investigators.DebugInfoParameterNameInvestigator;

public class DebugInfoParameterNameInvestigatorTest {

	@Test
	public void testParameterName() throws Exception {
		DebugInfoParameterNameInvestigator investigator = new DebugInfoParameterNameInvestigator();
		
		TestObj obj = new TestObj();
		Method method = TestObj.class.getMethod("test1", String.class, Integer[].class, List.class);
		
		assertEquals("one", investigator.determineParameterName(obj, method, String.class, null, null, 0));
		assertEquals("two", investigator.determineParameterName(obj, method, String.class, null, null, 1));
		assertEquals("three", investigator.determineParameterName(obj, method, String.class, null, null, 2));
		
		assertNull(investigator.determineParameterName(obj, method, String.class, null, null, null));
		assertNull(investigator.determineParameterName(obj, method, String.class, null, null, -1));
		assertNull(investigator.determineParameterName(obj, method, String.class, null, null, 3));
	}
}
