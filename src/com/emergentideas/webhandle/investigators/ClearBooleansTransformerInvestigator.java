package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.emergentideas.webhandle.ClearBooleans;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.transformers.ClearBooleansValueTransformer;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

public class ClearBooleansTransformerInvestigator implements
		ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus,
			Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		for(Annotation an : parameterAnnotations) {
			if(an instanceof ClearBooleans) {
				Map<String, Object> props = new HashMap<String, Object>();
				ClearBooleans cb = (ClearBooleans)an;
				props.put("clear", cb.value());
				props.put("dontClear", cb.dontClear());
				props.put("clearValue", cb.clearValue());
				return new TransformerSpecification[] { new TransformerSpecification(ClearBooleansValueTransformer.TRANSFORMER_NAME, props)};
			}
		}
		return null;
	}

}
