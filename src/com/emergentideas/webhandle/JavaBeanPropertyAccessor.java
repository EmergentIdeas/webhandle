package com.emergentideas.webhandle;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public class JavaBeanPropertyAccessor implements PropertyAccessor {

	public Object get(Object focus, String propertyName) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, focus.getClass());
			return pd.getReadMethod().invoke(focus, null);
		}
		catch(IntrospectionException e) {
			// This bean doesn't seem to have that property
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
		return null;
	}

	public boolean canAccess(Object focus, String propertyName) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(propertyName, focus.getClass());
			if(pd != null) {
				return true;
			}
		}
		catch(Exception e) {
			// not a big deal, just means we can't access the property
		}
		return false;
	}
	
	

}
