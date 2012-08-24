package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Entity;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterNameInvestigator;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

public class BeanPropertyNameAndDbTransformerInvestigator implements
		ParameterNameInvestigator, ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus, Method method, Class<T> parameterClass, 
			String parameterName, Annotation[] parameterAnnotations, InvocationContext context) {
		if(determineParameterName(focus, method, parameterClass, parameterAnnotations, context, null) == null) {
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
			InvocationContext context, Integer argumentIndex) {
		// Here we'll be looking for setter method.  If there's more than one argument, then we don't have
		// a setter by this definition
		
		if(method.getParameterTypes().length != 1) {
			return null;
		}
		
		return ReflectionUtils.getPropertyName(method);
	}

}
