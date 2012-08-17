package com.emergentideas.location.investigators;

import java.lang.reflect.Method;

import org.junit.Test;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.Name;
import com.emergentideas.location.TestObj;

import static org.junit.Assert.*;

public class NameAnnotationPropertyNameInvestigatorTest {

	@Test
	public void testGetParameterName() throws Exception {
		
		NameAnnotationPropertyNameInvestigator investigator = new NameAnnotationPropertyNameInvestigator();
		InvocationContext context = new InvocationContext();
		
		
		Method mTest1 = TestObj.class.getMethod("test1", String.class, String.class);
		String name = investigator.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0], context);
		assertEquals("one", name);
		
		name = investigator.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[1], context);
		assertNull(name);
	}
	
	

}
