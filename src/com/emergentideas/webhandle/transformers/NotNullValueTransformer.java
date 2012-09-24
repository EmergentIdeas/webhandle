package com.emergentideas.webhandle.transformers;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.NamedTransformer;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

@NamedTransformer(NotNullValueTransformer.TRANSFORMER_NAME)
public class NotNullValueTransformer implements ValueTransformer<Object, Object, Object[]> {

	public static final String TRANSFORMER_NAME = "not-null-transformer";
	
	protected Logger log = SystemOutLogger.get(NotNullValueTransformer.class);
	
	public Object[] transform(InvocationContext context,
			Map<String, Object> transformationProperties, Class finalParameterClass, String parameterName,
			Object... source) throws TransformationException {
		
		if(source == null) {
			Class c = (Class)transformationProperties.get("class");
			if(c == null) {
				return source;
			}
			
			if(List.class.isAssignableFrom(finalParameterClass)) {
				return (Object[])Array.newInstance(c, 0);
			}
			
			Object[] result = (Object[])Array.newInstance(c, 1);
			
			try {
				result[0] = c.newInstance();
				return result;
			}
			catch(Exception e) {
				log.error("Could not instantiate an array of class: " + c.getName(), e);
			}
		}
		
		return source;
	}
}
