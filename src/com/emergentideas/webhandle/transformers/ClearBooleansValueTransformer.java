package com.emergentideas.webhandle.transformers;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.NamedTransformer;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.exceptions.TransformationException;

@NamedTransformer(ClearBooleansValueTransformer.TRANSFORMER_NAME)
public class ClearBooleansValueTransformer implements ValueTransformer<Object, Object, Object[]> {

	public static final String TRANSFORMER_NAME = "clear-booleans-transformer";
	
	protected Logger log = SystemOutLogger.get(ClearBooleansValueTransformer.class);
	
	public Object[] transform(InvocationContext context,
			Map<String, Object> transformationProperties, Class finalParameterClass, String parameterName,
			Object... source) throws TransformationException {
		
		if(source != null) {
			String[] clear = (String[])transformationProperties.get("clear");
			String[] dontClear = (String[])transformationProperties.get("dontClear");
			Boolean clearValue = (Boolean)transformationProperties.get("clearValue");
			
			for(Object o : source) {
				try {
					for(Method m : o.getClass().getMethods()) {
						if(ReflectionUtils.isSetterMethod(m) == false) {
							continue;
						}
						if(ReflectionUtils.hasAnnotation(m, NoInject.class)) {
							continue;
						}
						Class parameterClass = m.getParameterTypes()[0]; 
						if(parameterClass.equals(Boolean.class) || parameterClass.equals(Boolean.TYPE)) {
							String memberName = ReflectionUtils.getPropertyName(m);
							if(ArrayUtils.contains(dontClear, memberName)) {
								continue;
							}
							if(clear.length == 0 || ArrayUtils.contains(clear, memberName)) {
								m.invoke(o, clearValue);
							}
						}
					}
				}
				catch(Exception e) {
					log.error("Could not clear booleans: " + o.getClass().getName(), e);
				}
			}
		}
		
		return source;
	}
}
