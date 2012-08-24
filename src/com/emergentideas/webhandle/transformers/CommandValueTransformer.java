package com.emergentideas.webhandle.transformers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.ParameterNotFoundException;
import com.emergentideas.webhandle.exceptions.TransformationException;

public class CommandValueTransformer implements ValueTransformer<String, Object, Object[]> {

	public Object[] transform(InvocationContext context,
			Map<String, String> transformationProperties, Class finalParameterClass, String parameterName,
			Object... source) throws TransformationException {
		
		if(source == null || source.length == 0) {
			return source;
		}
		
		ParameterMarshal pm = context.getFoundParameter(ParameterMarshal.class);
		
		
		for(Object o : source) {
			for(Method m : getSetterMethods(o.getClass())) {
				try {
					pm.call(o, m, true);
				}
				catch(ParameterNotFoundException e) {
					// no problem really, we just don't have any values to inject
				}
				catch(Exception e) {
					throw new TransformationException("Problem when calling method: " + m.getName(), e);
				}
			}
		}
		
		return source;
	}
	
	/**
	 * Gets method which have the setter name pattern, are public, and are not annotated with {@link NoInject}
	 * @param type
	 * @return
	 */
	protected List<Method> getSetterMethods(Class type) {
		List<Method> result = new ArrayList<Method>();
		
		for(Method m : type.getMethods()) {
			if(ReflectionUtils.isSetterMethod(m) && 
					Modifier.isPublic(m.getDeclaringClass().getModifiers()) && ReflectionUtils.hasAnnotation(m, NoInject.class) == false) {
				result.add(m);
			}
		}
		
		return result;
	}
	
}
