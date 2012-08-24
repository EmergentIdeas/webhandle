package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.emergentideas.utils.ReflectionUtils;

public class HandleAnnotationHandlerInvestigatorTest {

	@Test
	public void testFindMethod() {
		
		Method one = ReflectionUtils.getFirstMethod(Handler1.class, "one");
		Method two = ReflectionUtils.getFirstMethod(Handler1.class, "two");
		Method three = ReflectionUtils.getFirstMethod(Handler2.class, "three");
		Method four = ReflectionUtils.getFirstMethod(Handler2.class, "four");
		
		
		HandleAnnotationHandlerInvestigator investigator = new HandleAnnotationHandlerInvestigator();
		investigator.analyzeObject(new Handler1());
		
		handler1Tests(investigator, one, two);
		
		investigator = new HandleAnnotationHandlerInvestigator();
		// add the second handler first.  Since it has a class handler prefix our previous tests should still
		// give the same results
		investigator.analyzeObject(new Handler2());
		investigator.analyzeObject(new Handler1());

		handler1Tests(investigator, one, two);
		
		assertNull(investigator.determineHandler("/1/hello", HttpMethod.GET));
		assertEquals(three, investigator.determineHandler("/1/one/12", HttpMethod.GET).getMethod());
		
		// make sure we examine them in order
		assertEquals(three, investigator.determineHandler("/2/one", HttpMethod.GET).getMethod());
		
		assertEquals(four, investigator.determineHandler("/2/one", HttpMethod.POST).getMethod());
	}
	
	protected void handler1Tests(HandlerInvestigator investigator, Method one, Method two) {
		assertNull(investigator.determineHandler("/hello", HttpMethod.GET));
		assertEquals(one, investigator.determineHandler("/one/12", HttpMethod.GET).getMethod());
		
		// make sure we examine them in order
		assertEquals(one, investigator.determineHandler("/one", HttpMethod.GET).getMethod());
		
		assertEquals(two, investigator.determineHandler("/one", HttpMethod.POST).getMethod());
	}
}
