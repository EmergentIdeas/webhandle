package com.emergentideas.webhandle.transformers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.NamedTransformer;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.ValueTransformer;

@NamedTransformer("inject-transformer")
public class InjectValueTransformer extends CommandValueTransformer implements ValueTransformer<String, Object, Object[]> {
	/**
	 * Gets method which have the setter name pattern, are public, and are not annotated with {@link NoInject}
	 * @param type
	 * @return
	 */
	protected List<Method> getSetterMethods(Class type) {
		List<Method> result = new ArrayList<Method>();
		
		for(Method m : type.getMethods()) {
			if(ReflectionUtils.isSetterMethod(m) && 
					ReflectionUtils.isPublic(m) && ReflectionUtils.hasAnnotation(m, NoInject.class) == false) {
				
				Entity e = ReflectionUtils.getAnnotationOnClass(m.getParameterTypes()[0], Entity.class);
				
				if(e == null) {
					result.add(m);
				}
			}
		}
		
		return result;
	}

}
