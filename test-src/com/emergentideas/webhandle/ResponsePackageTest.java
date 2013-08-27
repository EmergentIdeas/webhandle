package com.emergentideas.webhandle;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.emergentideas.webhandle.assumptions.oak.HandleCaller;
import com.emergentideas.webhandle.assumptions.oak.LibraryTemplateSource;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.handlers.HandleAnnotationHandlerInvestigator;
import com.emergentideas.webhandle.handlers.Handler4;
import com.emergentideas.webhandle.handlers.TestServletOutputStream;
import com.emergentideas.webhandle.investigators.TemplateOutputTransformersInvestigator;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateInstance;

public class ResponsePackageTest {

	@Test
	public void testWithoutResponsePackage() throws Exception {
		assertTrue(makeCall("/hi").startsWith("<!DOCTYPE"));
	}
	
	@Test
	public void testResponsePackage() throws Exception {
		assertEquals("there", makeCall("/hello").trim());
	}
	
	protected String makeCall(String url) throws Exception {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateOutputTransformersInvestigator outputInvestigator = new TemplateOutputTransformersInvestigator();
		HandleAnnotationHandlerInvestigator handlerInvestigator = new HandleAnnotationHandlerInvestigator();
		
		Handler4 handler = new Handler4();
		handlerInvestigator.analyzeObject(handler);
		
		HandleCaller caller = new HandleCaller();
		caller.setHandlerInvestigator(handlerInvestigator);
		caller.setOutputInvestigator(outputInvestigator);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		when(request.getServletPath()).thenReturn(url);
		when(request.getMethod()).thenReturn("GET");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getOutputStream()).thenReturn(new TestServletOutputStream(out));
		
		context.setFoundParameter(HttpServletRequest.class, request);
		context.setFoundParameter(HttpServletResponse.class, response);
		
		LibraryTemplateSource lts = new LibraryTemplateSource();
		context.setFoundParameter(LibraryTemplateSource.class, lts);
		new WebAppLocation(context.getLocation()).setTemplateSource(lts);
		
		lts.add("one", new TemplateInstance() {
			
			public void render(SegmentedOutput output, Location location,
					String elementSourceName, String... processingHints) {
				output.getStream("body").append("there");
			}
		});

		caller.call(null, request, response, marshal);
		
		String written = new String(out.toByteArray(), "UTF-8");
		return written;
	}
	
	

}
