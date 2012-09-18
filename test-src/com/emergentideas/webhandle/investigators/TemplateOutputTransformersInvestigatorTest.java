package com.emergentideas.webhandle.investigators;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.handlers.Handler1;
import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.HtmlDocRespondent;
import com.emergentideas.webhandle.output.IterativeOutputCreator;
import com.emergentideas.webhandle.output.Respondent;
import com.emergentideas.webhandle.transformers.InputValuesTransformer;
import com.emergentideas.webhandle.transformers.TemplateTransformer;
import com.emergentideas.webhandle.transformers.WrapTransformer;

public class TemplateOutputTransformersInvestigatorTest {

	@Test
	public void testTemplateAnnotationFinding() {
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateOutputTransformersInvestigator investigator = new TemplateOutputTransformersInvestigator();
		
		Handler1 handler = new Handler1();
		Method handlerMethod = ReflectionUtils.getFirstMethod(Handler1.class, "one");
		
		
		Respondent resp = investigator.determineTransformers(context, handler, handlerMethod, "test");
		
		assertTrue(resp instanceof DirectRespondent);
		
		DirectRespondent dr = (DirectRespondent)resp;
		assertEquals("test", dr.getOutput());
		
		handlerMethod = ReflectionUtils.getFirstMethod(Handler1.class, "two");
		resp = investigator.determineTransformers(context, handler, handlerMethod, "test");
		
		assertTrue(resp instanceof IterativeOutputCreator);
		
		IterativeOutputCreator ioc = (IterativeOutputCreator)resp;
		assertTrue(ioc.getTransformers().get(0).getFocus() instanceof TemplateTransformer);
		assertTrue(ioc.getTransformers().get(1).getFocus() instanceof InputValuesTransformer);
		assertTrue(ioc.getTransformers().get(2).getFocus() instanceof WrapTransformer);
		
		assertTrue(ioc.getFinalRespondent() instanceof HtmlDocRespondent);
		
		
		resp = investigator.determineTransformers(context, handler, handlerMethod, new DirectRespondent("hello"));
		assertEquals("hello", ((DirectRespondent)resp).getOutput());
		
	}
}
