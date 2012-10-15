package com.emergentideas.webhandle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.transformers.TransformerParameterDataTransformer;
import com.emergentideas.webhandle.transformers.URLPropertiesTransformerPropertyDataTransformer;

public class ParameterMarshalConfiguration {

	// holds named transformers for converting and transforming request and return data
	protected Map<String, ValueTransformer<?, ?, ?>> transformers = Collections.synchronizedMap(new HashMap<String, ValueTransformer<?,?,?>>());
	
	// holds transforms that a generic for converting between types
	protected List<ValueTransformer<?, ?, ?>> typeTransformers = Collections.synchronizedList(new ArrayList<ValueTransformer<?,?,?>>());
	
	protected List<ParameterNameInvestigator> parameterNameInvestigators = Collections.synchronizedList(new ArrayList<ParameterNameInvestigator>());
	
	protected List<SourceSetInvestigator> sourceSetInvestigators = Collections.synchronizedList(new ArrayList<SourceSetInvestigator>());
	
	protected List<ParameterTransformersInvestigator> transformersInvestigators = Collections.synchronizedList(new ArrayList<ParameterTransformersInvestigator>());
	
	protected List<ObjectorInvestigator> objectorInvestigators = Collections.synchronizedList(new ArrayList<ObjectorInvestigator>());
	
	protected TransformerParameterDataTransformer transformerPropertiesDataTransformer = new URLPropertiesTransformerPropertyDataTransformer();
	
	/**
	 * Holds the cached generic method arguments which are moderately expensive to calculate
	 */
	protected Map<Method, Type[]> cachedMethodArgumentTypes = Collections.synchronizedMap(new HashMap<Method, Type[]>());

	/**
	 * Holds the cached generic method return types which are moderately expensive to calculate
	 */
	protected Map<Method, Type> cachedMethodReturnType = Collections.synchronizedMap(new HashMap<Method, Type>());
	
	/**
	 * Holds the cached parameter annotations which are moderately expensive to calculate
	 */
	protected Map<Method, Annotation[][]> cachedParameterAnnotations = Collections.synchronizedMap(new HashMap<Method, Annotation[][]>());
	
	
	
	public Map<String, ValueTransformer<?, ?, ?>> getTransformers() {
		return transformers;
	}

	public List<ValueTransformer<?, ?, ?>> getTypeTransformers() {
		return typeTransformers;
	}

	public List<ParameterNameInvestigator> getParameterNameInvestigators() {
		return parameterNameInvestigators;
	}

	public List<SourceSetInvestigator> getSourceSetInvestigators() {
		return sourceSetInvestigators;
	}

	public List<ParameterTransformersInvestigator> getTransformersInvestigators() {
		return transformersInvestigators;
	}

	public TransformerParameterDataTransformer getTransformerPropertiesDataTransformer() {
		return transformerPropertiesDataTransformer;
	}

	public void setTransformerPropertyDataTransformer(
			TransformerParameterDataTransformer transformerPropertyDataTransformer) {
		this.transformerPropertiesDataTransformer = transformerPropertyDataTransformer;
	}

	public List<ObjectorInvestigator> getObjectorInvestigators() {
		return objectorInvestigators;
	}
	
	/**
	 * Returns the generic types for a method.  This is a relatively expensive call, so results are cached in the
	 * configuration.
	 * @param m
	 * @return
	 */
	public java.lang.reflect.Type[] getGenericParameterTypes(Method m) {
		Type[] result = cachedMethodArgumentTypes.get(m);
		if(result == null) {
			result = m.getGenericParameterTypes();
			cachedMethodArgumentTypes.put(m, result);
		}
		return result;
	}
	
	public Type getGenericReturnType(Method m) {
		Type result = cachedMethodReturnType.get(m);
		if(result == null) {
			result = m.getGenericReturnType();
			cachedMethodReturnType.put(m, result);
		}
		
		return result;
	}
	
	public Annotation[][] getParameterAnnotations(Method m) {
		Annotation[][] result = cachedParameterAnnotations.get(m);
		if(result == null) {
			result = m.getParameterAnnotations();
			cachedParameterAnnotations.put(m, result);
		}
		return result;
	}

}
