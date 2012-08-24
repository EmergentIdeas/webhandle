package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.Source;
import com.emergentideas.webhandle.Transformers;
import com.emergentideas.webhandle.transformers.TransformerSpecification;

public class TransformersAnnotationTransformersInvestigator implements
		ParameterTransformersInvestigator {

	public <T> TransformerSpecification[] determineTransformers(Object focus, Method method,
			Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		
		for(Annotation annotation : parameterAnnotations) {
			if(annotation instanceof Transformers) {
				
				Transformers anno = (Transformers)annotation;
				int numOfTransformers = anno.value().length;
				TransformerSpecification[] specs = new TransformerSpecification[numOfTransformers];
				for(int i = 0; i < numOfTransformers; i++) {
					TransformerSpecification spec = new TransformerSpecification();
					spec.setTransformerName(anno.value()[i]);
					spec.setTransformerProperties(parseProperties(anno.properties()));
					specs[i] = spec;
				}
				return specs;
			}
		}
		
		return null;
	}
	
	/**
	 * This is clearly horribly broken if it purports to be a general URL encoded query string 
	 * parser but it will do for now for simple things. 
	 * @param data
	 * @return
	 */
	protected Map<String,String> parseProperties(String data) {
		Map<String,String> result = new HashMap<String, String>();
		if(StringUtils.isBlank(data) == false) {
			try {
				for(String nv : data.split("&")) {
					try {
						String[] parts = nv.split("=");
						result.put(parts[0], parts[1]);
					}
					catch(Exception e) {
						// carry on
					}
				}
			}
			catch(Exception ex) {
				// carry on
			}
		}
		
		return result;
	}

}
