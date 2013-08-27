package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import static org.mockito.Mockito.*;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.assumptions.oak.HandleCaller;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.exceptions.CouldNotHandle;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.exceptions.TransformationException;
import com.emergentideas.webhandle.exceptions.UserRequiredException;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;

public class HandleCallerTest {

	@Test
	public void testMakeCall() throws Exception {
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateOutputTransformersInvestigator outputInvestigator = new TemplateOutputTransformersInvestigator();
		HandleAnnotationHandlerInvestigator handlerInvestigator = new HandleAnnotationHandlerInvestigator();
		
		Handler1 handler = new Handler1();
		handlerInvestigator.analyzeObject(handler);
		Handler1 handler2 = new Handler1();
		handler2.setCalledOnce(true);
		handlerInvestigator.analyzeObject(handler2);
		
		HandleCaller caller = new HandleCaller();
		caller.setHandlerInvestigator(handlerInvestigator);
		caller.setOutputInvestigator(outputInvestigator);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		when(request.getServletPath()).thenReturn("/one/12");
		when(request.getMethod()).thenReturn("GET");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out));
		
		context.setFoundParameter(HttpServletRequest.class, request);
		context.setFoundParameter(HttpServletResponse.class, response);

		caller.call(null, request, response, marshal);
		
		String written = new String(out.toByteArray(), "UTF-8");
		assertEquals("really", written);
		
		// check to be sure we go to the next handler when we see a could not handle exception
		when(request.getServletPath()).thenReturn("/three");
		final ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out2));
		
		caller.call(null, request, response, marshal);
		
		written = new String(out2.toByteArray(), "UTF-8");
		assertEquals("called twice", written);
		
		// add the exception handlers
		caller.getExceptionHandlers().put(SecurityException.class, ReflectionUtils.getFirstMethodCallSpec(this, "securityException"));
		caller.getExceptionHandlers().put(TransformationException.class, ReflectionUtils.getFirstMethodCallSpec(this, "transformationException"));
		caller.getExceptionHandlers().put(UserRequiredException.class, ReflectionUtils.getFirstMethodCallSpec(this, "userRequiredException"));

		// test a security exception
		when(request.getServletPath()).thenReturn("/five");
		final ByteArrayOutputStream out3 = new ByteArrayOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out3));
		
		caller.call(null, request, response, marshal);
		
		written = new String(out3.toByteArray(), "UTF-8");
		assertEquals("security exception", written);
		
		// test a transformation exception
		when(request.getServletPath()).thenReturn("/six");
		final ByteArrayOutputStream out4 = new ByteArrayOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out4));
		
		caller.call(null, request, response, marshal);
		
		written = new String(out4.toByteArray(), "UTF-8");
		assertEquals("transformation exception", written);
		
		
		// test be sure that the exception with the minimum distance is chosen
		when(request.getServletPath()).thenReturn("/twelve");
		final ByteArrayOutputStream out5 = new ByteArrayOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out5));
		
		caller.call(null, request, response, marshal);
		
		written = new String(out5.toByteArray(), "UTF-8");
		assertEquals("user required exception", written);

	}
	
	@Test
	public void testEnsureHandlersAddedLaterAreCalledFirst() throws Exception {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateOutputTransformersInvestigator outputInvestigator = new TemplateOutputTransformersInvestigator();
		HandleAnnotationHandlerInvestigator handlerInvestigator = new HandleAnnotationHandlerInvestigator();
		
		Handler1 handler = new Handler1();
		handlerInvestigator.analyzeObject(handler);
		Handler2 handler2 = new Handler2();
		handlerInvestigator.analyzeObject(handler2);
		
		HandleCaller caller = new HandleCaller();
		caller.setHandlerInvestigator(handlerInvestigator);
		caller.setOutputInvestigator(outputInvestigator);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		when(request.getServletPath()).thenReturn("/1/fourteen");
		when(request.getMethod()).thenReturn("GET");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out));
		
		context.setFoundParameter(HttpServletRequest.class, request);
		context.setFoundParameter(HttpServletResponse.class, response);

		caller.call(null, request, response, marshal);
		
		String written = new String(out.toByteArray(), "UTF-8");
		assertEquals("handler2", written);
	}
	
	@Test
	public void testThrowTimes() throws Exception {
		
		int iterations = 100000;
		long start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			try {
				throw new CouldNotHandleException();
			}
			catch(CouldNotHandleException e) {
				
			}
		}
		
		long stop = System.currentTimeMillis();
		
		System.out.println("Throw time (ms): " + (stop - start));
		
		start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			try {
				new CouldNotHandleException();
			}
			catch(CouldNotHandleException e) {
				
			}
		}
		
		stop = System.currentTimeMillis();
		
		System.out.println("Create and catch but no throw time (ms): " + (stop - start));

		start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			new CouldNotHandleException();
		}
		
		stop = System.currentTimeMillis();
		
		System.out.println("Create no catch, no throw time (ms): " + (stop - start));

		start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			new CouldNotHandle() {
			};
		}
		
		stop = System.currentTimeMillis();
		
		System.out.println("Create could not handle time (ms): " + (stop - start));

		start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
			new ArrayList();
		}
		
		stop = System.currentTimeMillis();
		
		System.out.println("Create array list time (ms): " + (stop - start));

		start = System.currentTimeMillis();
		for(int i = 0; i < iterations; i++) {
		}
		
		stop = System.currentTimeMillis();
		
		System.out.println("Raw loop time (ms): " + (stop - start));

	}
	
	
	public String securityException(Exception exception) {
		return "security exception";
	}
	
	public String transformationException() {
		return "transformation exception";
	}
	
	public String userRequiredException() {
		return "user required exception";
	}

}


