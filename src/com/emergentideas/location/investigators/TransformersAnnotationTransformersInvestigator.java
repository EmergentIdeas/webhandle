package com.emergentideas.location.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.Source;
import com.emergentideas.location.Transformers;
import com.emergentideas.location.TransformersInvestigator;

public class TransformersAnnotationTransformersInvestigator implements
		TransformersInvestigator {

	public <T> String[] determineTransformers(Object focus, Method method,
			Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Transformers) {
				return ((Transformers)annotation).value();
			}
		}
		
		return null;
	}

}
