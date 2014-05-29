package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;

public class J2EEAnnotationHandlerInvestigatorTest {

	@Test
	public void testFindMethod() {
		
		Method one = ReflectionUtils.getFirstMethod(Handler1J2EE.class, "one");
		Method two = ReflectionUtils.getFirstMethod(Handler1J2EE.class, "two");
		Method three = ReflectionUtils.getFirstMethod(Handler2J2EE.class, "three");
		Method four = ReflectionUtils.getFirstMethod(Handler2J2EE.class, "four");
		
		
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		investigator.analyzeObject(new Handler1J2EE());
		
		handler1Tests(investigator, one, two);
		
		investigator = new HandleAnnotationHandlerInvestigator();
		// add the second handler first.  Since it has a class handler prefix our previous tests should still
		// give the same results
		investigator.analyzeObject(new Handler2J2EE());
		investigator.analyzeObject(new Handler1J2EE());

		handler1Tests(investigator, one, two);
		
		assertEquals(0, investigator.determineHandlers("/1/hello", HttpMethod.GET).length);
		
		CallSpec[] specs = investigator.determineHandlers("/1/one/12", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(three, specs[0].getMethod());
		
		// make sure we examine them in order
		specs = investigator.determineHandlers("/2/one", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(four, specs[0].getMethod());
		
		specs = investigator.determineHandlers("/2/one", HttpMethod.POST);
		assertEquals(1, specs.length);
		assertEquals(four, specs[0].getMethod());
	}
	
	protected void handler1Tests(HandlerInvestigator investigator, Method one, Method two) {
		
		CallSpec[] specs = investigator.determineHandlers("/hello", HttpMethod.GET); 
		assertEquals(0, specs.length);
		
		specs = investigator.determineHandlers("/one/12", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(one, specs[0].getMethod());
		
		// make sure we examine them in order
		specs = investigator.determineHandlers("/one", HttpMethod.GET);
		assertEquals(1, specs.length);
		assertEquals(two, specs[0].getMethod());
		
		specs = investigator.determineHandlers("/one", HttpMethod.POST);
		assertEquals(1, specs.length);
		assertEquals(two, specs[0].getMethod());
	}
	
	@Test
	public void testFindMostRecentMethodForUrl() throws Exception {
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		investigator.analyzeObject(new Handler3J2EE());
		
		CallSpec[] specs = investigator.determineHandlers("/1/two", HttpMethod.GET); 
		assertEquals(1, specs.length);
		

		
	}
	
	@Test
	public void testMethodOrder() throws Exception {
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		List<Method> methods = ReflectionUtils.getMethodsInReverseInheritenceOrder(Handler3J2EE.class);
		
		assertTrue(methods.get(methods.size() - 1).getName().equals("six"));
	}
}
