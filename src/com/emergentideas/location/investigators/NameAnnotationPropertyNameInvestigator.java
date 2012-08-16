package com.emergentideas.location.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.Name;
import com.emergentideas.location.PropertyNameInvestigator;

public class NameAnnotationPropertyNameInvestigator implements
		PropertyNameInvestigator {

	public <T> String determinePropertyName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context) {
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Name) {
				return ((Name)annotation).value();
			}
		}
		
		return null;
	}

}
