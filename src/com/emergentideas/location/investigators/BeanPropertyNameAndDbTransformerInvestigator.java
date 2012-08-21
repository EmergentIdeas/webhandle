package com.emergentideas.location.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Entity;

import com.emergentideas.location.Constants;
import com.emergentideas.location.InvocationContext;
import com.emergentideas.location.ParameterNameInvestigator;
import com.emergentideas.location.ParameterTransformersInvestigator;
import com.emergentideas.location.transformers.TransformerSpecification;

public class BeanPropertyNameAndDbTransformerInvestigator implements
		ParameterNameInvestigator, ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus, Method method, Class<T> parameterClass, 
			String parameterName, Annotation[] parameterAnnotations, InvocationContext context) {
		if(determineParameterName(focus, method, parameterClass, parameterAnnotations, context) == null) {
			// if this isn't a setter method that will give us a bean name then we don't want to try 
			// the transformation
			return null;
		}
		
		for(Annotation anno : method.getParameterTypes()[0].getAnnotations()) {
			if(anno instanceof Entity) {
				return new TransformerSpecification[] {new TransformerSpecification(Constants.DB_TO_OBJECT_TRANSFORMER_NAME_DEFAULT)}; 
			}
		}
		return null;
	}
	
	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context) {
		// Here we'll be looking for setter method.  If there's more than one argument, then we don't have
		// a setter by this definition
		
		if(method.getParameterTypes().length != 1) {
			return null;
		}
		
		String methodName = method.getName();
		
		if(methodName.startsWith("set") && methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3))) {
			String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
			return propertyName;
		}
		
		// we couldn't find a parameter name
		return null;
	}

}
