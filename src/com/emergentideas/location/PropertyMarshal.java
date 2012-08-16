package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.emergentideas.location.exceptions.TransformationException;
import com.emergentideas.location.transformers.ArrayToObjectTransformer;
import com.emergentideas.location.transformers.StringArrayToStringTransformer;
import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;


public class PropertyMarshal {

	// holds the named value sources
	protected Map<String, ValueSource<? extends Object>> sources = Collections.synchronizedMap(new HashMap<String, ValueSource<? extends Object>>());
	
	// holds named transformers for converting and transforming request data
	protected Map<String, ValueTransformer<?, ?>> transformers = Collections.synchronizedMap(new HashMap<String, ValueTransformer<?,?>>());
	
	// holds transforms that a generic for converting between types
	protected List<ValueTransformer<?, ?>> typeTransformers = Collections.synchronizedList(new ArrayList<ValueTransformer<?,?>>());
	
	protected List<PropertyNameInvestigator> propertyNameInvestigators = Collections.synchronizedList(new ArrayList<PropertyNameInvestigator>());
	
	protected List<SourceSetInvestigator> sourceSetInvestigators = Collections.synchronizedList(new ArrayList<SourceSetInvestigator>());
	
	protected List<TransformersInvestigator> transformersInvestigators = Collections.synchronizedList(new ArrayList<TransformersInvestigator>());
	
	// the invocation context for the larger request
	protected InvocationContext context;
	
	protected Logger log = SystemOutLogger.get(this.getClass());
	
	protected StringArrayToStringTransformer stringJoiner = new StringArrayToStringTransformer();
	protected ArrayToObjectTransformer arrayToObject = new ArrayToObjectTransformer();
	
	
	public <T> String determinePropertyName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations) {
		for(PropertyNameInvestigator investigator : propertyNameInvestigators) {
			String name = investigator.determinePropertyName(focus, method, parameterClass, parameterAnnotations, context);
			if(name != null) {
				return name;
			}
		}
		
		return null;
	}
	
	public <T> Class<T> determineParameterClass(Object focus, Method method, int index) {
		Type type = method.getParameterTypes()[index];
		if(type instanceof Class) {
			return (Class)type;
		}
		
		return null;
	}
	
	public <T> String[] determineAllowedSourceSets(Object focus, Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations) {
		for(SourceSetInvestigator investigator : sourceSetInvestigators) {
			String[] sets = investigator.determineAllowedSourceSets(focus, method, parameterClass, parameterName, parameterAnnotations, context);
			if(sets != null) {
				return sets;
			}
		}
		
		return null;
	}
	
	public <T> String[] determineTransformers(Object focus, Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations) {
		List<String> allTransformers = new ArrayList<String>();
		
		for(TransformersInvestigator investigator : transformersInvestigators) {
			String[] transformers = investigator.determineTransformers(focus, method, parameterClass, parameterName, parameterAnnotations, context);
			if(transformers != null) {
				addAll(allTransformers, transformers);
			}
		}
		
		return allTransformers.toArray(new String[allTransformers.size()]);
	}
	
	protected <T> void addAll(Collection<T> col, T[] ar) {
		for(T t : ar) {
			if(t != null) {
				col.add(t);
			}
		}
	}
	
	public <T, O> T getTypedParameter(String name, Class<T> finalType, O initial, String ... transformersToRun) {
		 
		if(transformersToRun == null) {
			transformersToRun = new String[0];
		}
		
		Object[] startingPoint;
		if(isArrayType(initial)) {
			startingPoint = (Object[])initial;
		}
		else {
			startingPoint = makeArrayFromObject(initial);
		}
		
		// Start applying the named transformers first.
		// They may convert the data type as they're run to the correct type
		
		List<String> transformersRun = new ArrayList<String>();
		startingPoint = runTransformers(startingPoint, transformersRun, transformersToRun);
		
		Class finalTypeAsArray = getArrayStyle(finalType);
		
		// If our type isn't right, let's convert it now
		if(isReturnTypeCompatible(startingPoint.getClass(), finalTypeAsArray) == false) {
			// We can't assign the current array to an array type for the parameter.  We'll have to convert
			startingPoint = makeArrayFromObject(convert(startingPoint, finalTypeAsArray));
		}
		
		// if they're are any transformers we haven't applied, maybe they needed to be applied after they were converted
		// so let's apply them now
		startingPoint = runTransformers(startingPoint, transformersRun, transformersToRun);
		
		
		if(isReturnTypeCompatible(startingPoint.getClass(), finalTypeAsArray) == false) {
			// we couldn't get converted and transformed to the destination data type
			log.error("Couldn't convert parameter " + name + " type " + initial.getClass().getName() + " to type " + 
			finalType.getName() + "and apply transformers: " + stringJoiner.transform(null, null, transformersToRun));
			return null;
		}
		
		return convertArrayToSingleValueIfNeeded(startingPoint, finalType);
	}
	
	protected <T> T convertArrayToSingleValueIfNeeded(Object data, Class finalType) {
		if(data.getClass().isArray() && finalType.getClass().isArray() == false) {
			// we've been working with arrays, but we want a single value
			
			// let's try an automatic convert
			Object result = convert((Object[])data, finalType);
			if(isReturnTypeCompatible(result.getClass(), finalType)) {
				return (T)result;
			}
			
			// So, no auto convert, let's just hand back the first one then
			return (T)arrayToObject.transform(context, null, (Object[])data);
		}
		
		// Looks like the desired return type was an array of the same type, so we'll return our array
		return (T)data;
	}
	
	/**
	 * Converts an array of data to the destination type
	 * @param data The data to convert
	 * @param targetType The type, an array or ordinary object, that we're converting to
	 * @return
	 */
	protected Object convert(Object[] data, Class targetType) {
		Object result = data;
		for(ValueTransformer transformer : typeTransformers) {
			if(canConvert(data, targetType, transformer)) {
				result = transformer.transform(null, null, data);
				if(targetType.isArray()) {
					result = makeArrayFromObject(result);
				}
				break;
			}
		}
		return result;
	}
	
	/**
	 * Returns true if <code>data</code> can be converted to <code>finalType</code> by <code>transformer</code>
	 * @param data
	 * @param finalType
	 * @param transformer
	 * @return
	 */
	protected boolean canConvert(Object[] data, Class finalType, ValueTransformer transformer) {
		if(canRun(data, transformer) == false) {
			return false;
		}
		
		return isReturnTypeCompatible(finalType, transformer);
	}
	
	/**
	 * Runs <code>transformersToRun</code> except those in <code>transformersRun</code> on <code>data</code> adding those
	 * transforms run to the list as they're run.
	 * @param data The data to transform
	 * @param transformersRun The list of transformers that have been run already and should not be run again
	 * @param transformersToRun The transformers to run
	 * @return
	 */
	protected Object[] runTransformers(Object[] data, List<String> transformersRun, String ... transformersToRun) {
		for(String transformerName : transformersToRun) {
			if(transformersRun.contains(transformerName)) {
				continue;
			}
			
			ValueTransformer transformer = transformers.get(transformerName);
			if(transformer != null) {
				try {
					if(canRun(data, transformer)) {
						data = makeArrayFromObject(transformer.transform(null, null, data));
						transformersRun.add(transformerName);
					}
				}
				catch(TransformationException e) {
					log.error("Error transforming data with transformer: " + transformerName, e);
				}
			}
		}
		
		return data;
	}
	
	/**
	 * Returns true if the transformer can be run on the data, that is, the data
	 * type is compatible with the transformer signature.
	 * @param data Data to transform
	 * @param transformer
	 * @return
	 */
	protected boolean canRun(Object[] data, ValueTransformer transformer) {
		
		Method method = getFirstMethod(transformer.getClass(), "transform");
		if(method == null) {
			return false;
		}
		
		Class argClass = null;
		
		Type argType = method.getGenericParameterTypes()[2];
		
		if(argType instanceof Class) {
			argClass = (Class)argType;
		}
		else if(argType instanceof GenericArrayType) {
			argClass = (Class)((GenericArrayType)argType).getGenericComponentType();
		}
		
		return getArrayStyle(argClass).isAssignableFrom(data.getClass());
	}
	
	/**
	 * Returns true if <code>finalType</code> is a base type for the type returned
	 * from <code>transformer</code>.
	 * @param finalType The type of the variable we'll be assigning to
	 * @param transformer The transformer that will produce the value
	 * @return
	 */
	protected boolean isReturnTypeCompatible(Class finalType, ValueTransformer transformer) {
		
		Method method = getFirstMethod(transformer.getClass(), "transform");
		if(method == null) {
			return false;
		}
		
		Type argType = method.getGenericReturnType();
		Class argClass = null;
		if(argType instanceof Class) {
			argClass = (Class)argType;
		}
		else if(argType instanceof ParameterizedType) {
			
		}
		else if(argType instanceof GenericArrayType) {
			argClass = getArrayStyle((Class)((ParameterizedType)transformer.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		}
		
		return isReturnTypeCompatible(argClass, finalType);
	}
	
	protected boolean isReturnTypeCompatible(Class currentDataType, Class returnDataType) {
		return returnDataType.isAssignableFrom(currentDataType);
	}
	
	protected <T> Method getFirstMethod(Class<T> focus, String methodName) {
		for(Method m : focus.getMethods()) {
			if(m.getName().equals(methodName)) {
				return m;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the array version of a class if the type passed is not already an array class.  For instance,
	 * if Integer.class is passed, the return value is Integer[].class.  If Integer[].class is passed, the
	 * return value is Integer[].class.
	 * @param type The type to convert.
	 * @return
	 */
	protected Class getArrayStyle(Class type) {
		if(type.getName().startsWith("[")) {
			// Okay, it is an array, so just return it
			return type;
		}
		return Array.newInstance(type, 0).getClass();
	}
	
	/**
	 * Makes a single object into an array of those objects (length 1) if it is not already an array.
	 * @param o
	 * @return
	 */
	protected <O> O[] makeArrayFromObject(O o) {
		if(o.getClass().isArray()) {
			return (O[])o;
		}
		
		O[] result = (O[])Array.newInstance(o.getClass(), 1);
		result[0] = o;
		return result;
	}
	
	/**
	 * returns true if the object is an array of something
	 * @param o
	 * @return
	 */
	protected boolean isArrayType(Object o) {
		return o.getClass().isArray();
	}
	
	
	/**
	 * Finds an initial Object based on the sources available, the name and type of the parameter, and the sources which are allowed
	 * @param name The name of property (optional)
	 * @param type The type of the property (optional)
	 * @param allowedSources
	 * @return
	 */
	public <T> Object getInitialObject(String name, Class<T> type, String ... allowedSources) {
		for(String sourceName : sources.keySet()) {
			if(isSourceAllowed(sourceName, allowedSources)) {
				ValueSource source = sources.get(sourceName);
				if(source.canGet(name, type, context)) {
					return source.get(name, type, context);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns true if the source name is among the array of allowed sources
	 * @param sourceName
	 * @param allowedSources
	 * @return
	 */
	protected boolean isSourceAllowed(String sourceName, String[] allowedSources) {
		if(allowedSources == null) {
			return true;
		}
		
		if(allowedSources.length == 0) {
			return true;
		}
		
		return contains(sourceName, allowedSources);
	}
	
	/**
	 * Returns true if <code>focus</code> is in the <code>list</code>
	 * @param focus
	 * @param list
	 * @return
	 */
	protected boolean contains(String focus, String[] list) {
		for(String s : list) {
			if(focus == s) {
				return true;
			}
			if(s == null) {
				continue;
			}
			
			if(s.equals(focus)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public void addSource(String name, ValueSource<? extends Object> source) {
		sources.put(name, source);
	}

	public Map<String, ValueSource<? extends Object>> getSources() {
		return sources;
	}

	public Map<String, ValueTransformer<?, ?>> getTransformers() {
		return transformers;
	}

	public List<ValueTransformer<?, ?>> getTypeTransformers() {
		return typeTransformers;
	}

	public InvocationContext getContext() {
		return context;
	}

	public void setContext(InvocationContext context) {
		this.context = context;
	}

	public List<PropertyNameInvestigator> getPropertyNameInvestigators() {
		return propertyNameInvestigators;
	}

	public List<SourceSetInvestigator> getSourceSetInvestigators() {
		return sourceSetInvestigators;
	}

	public List<TransformersInvestigator> getTransformersInvestigators() {
		return transformersInvestigators;
	}

	
}
