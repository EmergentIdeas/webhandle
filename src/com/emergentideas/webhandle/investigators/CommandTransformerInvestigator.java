package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.webhandle.Command;
import com.emergentideas.webhandle.Inject;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

public class CommandTransformerInvestigator implements
		ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus,
			Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		for(Annotation an : parameterAnnotations) {
			if(an instanceof Command) {
				return new TransformerSpecification[] { new TransformerSpecification("command-transformer")};
			}
			if(an instanceof Inject) {
				return new TransformerSpecification[] { new TransformerSpecification("inject-transformer")};
			}
		}
		return null;
	}

}
