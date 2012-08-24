package com.emergentideas.webhandle.investigators;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;

import static org.junit.Assert.*;

public class NameAnnotationPropertyNameInvestigatorTest {

	@Test
	public void testGetParameterName() throws Exception {
		
		NameAnnotationPropertyNameInvestigator investigator = new NameAnnotationPropertyNameInvestigator();
		InvocationContext context = new InvocationContext();
		
		
		Method mTest1 = TestObj.class.getMethod("test1", String.class, Integer[].class, List.class);
		String name = investigator.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0], context, null);
		assertEquals("one", name);
		
		name = investigator.determineParameterName(null, mTest1, 
				Integer[].class, mTest1.getParameterAnnotations()[1], context, null);
		assertEquals("two", name);
	}
	
	

}
