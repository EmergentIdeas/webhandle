package com.emergentideas.webhandle.investigators;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.TestObj;
import com.emergentideas.webhandle.TestObj6;
import com.emergentideas.webhandle.investigators.NameAnnotationPropertyNameInvestigator;

import static org.junit.Assert.*;

public class ResourceAnnotationPropertyNameInvestigatorTest {

	@Test
	public void testGetParameterName() throws Exception {
		
		ResourceAnnotationPropertyNameInvestigator investigator = new ResourceAnnotationPropertyNameInvestigator();
		InvocationContext context = new InvocationContext();
		
		
		Method mTest1 = TestObj6.class.getMethod("setA", String.class);
		String name = investigator.determineParameterName(null, mTest1, 
				String.class, mTest1.getParameterAnnotations()[0], context, null);
		assertEquals("nametwo", name);
		
	}
	
	

}
