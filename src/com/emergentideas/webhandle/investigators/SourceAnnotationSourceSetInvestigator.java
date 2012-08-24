package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.Source;
import com.emergentideas.webhandle.SourceSetInvestigator;

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
