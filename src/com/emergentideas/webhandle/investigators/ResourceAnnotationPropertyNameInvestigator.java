package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.ParameterNameInvestigator;

public class ResourceAnnotationPropertyNameInvestigator implements
		ParameterNameInvestigator {

	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context, Integer argumentIndex) {
		if(ReflectionUtils.isSetterMethod(method)) {
			Resource r = ReflectionUtils.getAnnotation(method, Resource.class);
			if(r != null) {
				if(StringUtils.isBlank(r.name()) == false) {
					return r.name();
				}
			}
		}
		
		
		return null;
	}

}
