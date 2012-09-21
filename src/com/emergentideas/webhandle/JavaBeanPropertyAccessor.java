package com.emergentideas.webhandle;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.emergentideas.utils.ReflectionUtils;

public class JavaBeanPropertyAccessor implements PropertyAccessor {

	public Object get(Object focus, String propertyName) {
		try {
			Method m = ReflectionUtils.getGetterMethod(focus, propertyName);
			return m.invoke(focus, null);
		}
		catch(RuntimeException e) {
			// This is most likely an exception coming from the accessor method itself
			// Catching and rethrowing so that we can capture non-runtime exceptions
			throw e;
		}
		catch(Exception e) {
			// This is most likely an exception coming from the accessor method itself
			throw new RuntimeException(e);
		}
	}

	public boolean canAccess(Object focus, String propertyName) {
		return ReflectionUtils.getGetterMethod(focus, propertyName) != null;
	}
	
	

}
