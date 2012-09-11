package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.emergentideas.webhandle.Command;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.transformers.NotNullValueTransformer;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

public class NotNullTransformerInvestigator implements
		ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus,
			Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		for(Annotation an : parameterAnnotations) {
			if(an instanceof NotNull) {
				Map<String, Object> props = new HashMap<String, Object>();
				NotNull nn = (NotNull)an;
				if(nn.value().equals(Object.class)) {
					// since it just says the default of Object, use the class of the parameter
					props.put("class", parameterClass);
				}
				else {
					// a specific class has been declared, so we'll use that.
					props.put("class", nn.value());
				}
				return new TransformerSpecification[] { new TransformerSpecification(NotNullValueTransformer.TRANSFORMER_NAME, props)};
			}
		}
		return null;
	}

}
