package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.ParameterNameInvestigator;

public class NameAnnotationPropertyNameInvestigator implements
		ParameterNameInvestigator {

	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context, Integer argumentIndex) {
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Name) {
				return ((Name)annotation).value();
			}
		}
		
		return null;
	}

}
