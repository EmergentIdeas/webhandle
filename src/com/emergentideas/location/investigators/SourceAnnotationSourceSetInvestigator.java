package com.emergentideas.location.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.Name;
import com.emergentideas.location.Source;
import com.emergentideas.location.SourceSetInvestigator;

public class SourceAnnotationSourceSetInvestigator implements
		SourceSetInvestigator {

	public <T> String[] determineAllowedSourceSets(Object focus, Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Source) {
				return ((Source)annotation).value();
			}
		}
		
		return null;
	}

}
