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
import com.emergentideas.webhandle.transformers.TemplateTransformer;

public class TemplateOutputTransformersInvestigatorTest {

	@Test
	public void testTemplateAnnotationFinding() {
		InvocationContext context = new InvocationContext();
		ParameterMarshal marshal = new ParameterMarshal(new WebParameterMarsahalConfiguration(), context);
		new WebRequestContextPopulator().populate(marshal, context);
		
		TemplateTransformer transformer = new TemplateTransformer();
		Method method = ReflectionUtils.getFirstMethod(TemplateTransformer.class, "transform");
		
		TemplateOutputTransformersInvestigator investigator = new TemplateOutputTransformersInvestigator(transformer, method);
		
		Handler1 handler = new Handler1();
		Method handlerMethod = ReflectionUtils.getFirstMethod(Handler1.class, "one");
		
		
//		CallSpec[] specs = investigator.determineTransformers(handler, handlerMethod, annotation, context)
		
		
	}
}
