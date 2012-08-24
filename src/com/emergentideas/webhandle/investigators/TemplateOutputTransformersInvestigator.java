package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.OutputTransformersInvestigator;
import com.emergentideas.webhandle.output.Template;

/**
 * Finds specs for the methods that should be called to transform the output
 * into data in a segmented output.
 * @author kolz
 *
 */
public class TemplateOutputTransformersInvestigator implements
		OutputTransformersInvestigator {
	
	protected Object transformer;
	protected Method transform;
	
	public TemplateOutputTransformersInvestigator() {
		
	}
	
	public TemplateOutputTransformersInvestigator(Object transformer, Method transform) {
		this.transformer = transformer;
		this.transform = transform;
	}
	
	
	public CallSpec[] determineTransformers(Object focus, Method method,
			Annotation annotation, InvocationContext context) {
		if(isAnnotationCorrectType(annotation)) {
			CallSpec spec = new CallSpec(transformer, transform, false);
			Map<String, String> props = new HashMap<String, String>();
			addProperties(props, annotation);
			spec.setCallSpecificProperties(props);
			return new CallSpec[] { spec };
		}
		return null;
	}
	
	protected boolean isAnnotationCorrectType(Annotation annotation) {
		return annotation instanceof Template;
	}
	
	protected void addProperties(Map<String, String> props, Annotation annotation) {
	}

}
