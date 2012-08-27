package com.emergentideas.webhandle.handlers;

import static org.junit.Assert.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.ssi.ByteArrayServletOutputStream;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.exceptions.TransformationException;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;

public class HandleCallerTest {

	@Test
	public void testMakeCall() throws Exception {
		
		final ByteArrayServletOutputStream out = new ByteArrayServletOutputStream();
		
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateOutputTransformersInvestigator outputInvestigator = new TemplateOutputTransformersInvestigator();
		HandleAnnotationHandlerInvestigator handlerInvestigator = new HandleAnnotationHandlerInvestigator();
		
		Handler1 handler = new Handler1();
		handlerInvestigator.analyzeObject(handler);
		
		HandleCaller caller = new HandleCaller();
		caller.setHandlerInvestigator(handlerInvestigator);
		caller.setOutputInvestigator(outputInvestigator);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		when(request.getServletPath()).thenReturn("/one/12");
		when(request.getMethod()).thenReturn("GET");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(out);

		caller.call(null, request, response, marshal);
		
		String written = new String(out.toByteArray(), "UTF-8");
		assertEquals("really", written);
		
		// check to be sure we go to the next handler when we see a could not handle exception
		when(request.getServletPath()).thenReturn("/three");
		final ByteArrayServletOutputStream out2 = new ByteArrayServletOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(out2);
		
		caller.call(null, request, response, marshal);
		
		written = new String(out2.toByteArray(), "UTF-8");
		assertEquals("called twice", written);
		
		// add the exception handlers
		caller.getExceptionHandlers().put(SecurityException.class, ReflectionUtils.getFirstMethodCallSpec(this, "securityException"));
		caller.getExceptionHandlers().put(TransformationException.class, ReflectionUtils.getFirstMethodCallSpec(this, "transformationException"));

		// test a security exception
		when(request.getServletPath()).thenReturn("/five");
		final ByteArrayServletOutputStream out3 = new ByteArrayServletOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(out3);
		
		caller.call(null, request, response, marshal);
		
		written = new String(out3.toByteArray(), "UTF-8");
		assertEquals("security exception", written);
		
		// test a transformation exception
		when(request.getServletPath()).thenReturn("/six");
		final ByteArrayServletOutputStream out4 = new ByteArrayServletOutputStream();
		response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(out4);
		
		caller.call(null, request, response, marshal);
		
		written = new String(out4.toByteArray(), "UTF-8");
		assertEquals("transformation exception", written);
		
		
	}
	
	
	public String securityException(Exception exception) {
		return "security exception";
	}
	
	public String transformationException() {
		return "transformation exception";
	}
}
