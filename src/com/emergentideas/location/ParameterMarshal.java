package com.emergentideas.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.emergentideas.location.exceptions.TransformationException;
import com.emergentideas.location.transformers.ArrayToCollectionTransformer;
import com.emergentideas.location.transformers.ArrayToObjectTransformer;
import com.emergentideas.location.transformers.StringArrayToStringTransformer;
import com.emergentideas.location.transformers.TransformerSpecification;
import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;

/**
 * Assembles and transforms a set of parameters for a given method call.
 * @author kolz
 *
 */
public class ParameterMarshal {
	
	protected static final int TRANSFORMER_SOURCE_DATA_ARG_POSITION = 3;

	// holds the named value sources
	protected Map<String, ValueSource<? extends Object>> sources = Collections.synchronizedMap(new HashMap<String, ValueSource<? extends Object>>());
	
	// The set of transformers and investigators that this marshal should use.  These are generally not specific
	// to any one call put rather to a type or class of calls.
	protected ParameterMarshalConfiguration configuration;
	
	// the invocation context for the larger request
	protected InvocationContext context;
	
	protected Logger log = SystemOutLogger.get(this.getClass());
	
	protected StringArrayToStringTransformer stringJoiner = new StringArrayToStringTransformer();
	protected ArrayToObjectTransformer arrayToObject = new ArrayToObjectTransformer();
	protected ArrayToCollectionTransformer arrayToCollection = new ArrayToCollectionTransformer(); 
	
	
	public ParameterMarshal() {
		this(new ParameterMarshalConfiguration());
	}
	
	public ParameterMarshal(ParameterMarshalConfiguration config) {
		this.configuration = config;
	}
	
	public <T> String determineParameterName(Object focus, Method method, Class<T> parameterClass, Annotation[] parameterAnnotations) {
		for(ParameterNameInvestigator investigator : configuration.getParameterNameInvestigators()) {
			String name = investigator.determineParameterName(focus, method, parameterClass, parameterAnnotations, context);
			if(name != null) {
				return name;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns just the class of the parameter without any parameterization info
	 * @param focus The object on which the method will be called.
	 * @param method The method with the argument we're getting the type for
	 * @param index The index of the argument
	 * @return
	 */
	public Class determineParameterClass(Object focus, Method method, int index) {
		Type type = method.getParameterTypes()[index];
		if(type instanceof Class) {
			return (Class)type;
		}
		
		return null;
	}
	
	/**
	 * Returns they type with parameterization info
	 * @param focus The object on which the method will be called.
	 * @param method The method with the argument we're getting the type for
	 * @param index The index of the argument
	 * @return
	 */
	public Type determineParameterType(Object focus, Method method, int index) {
		Type type = method.getGenericParameterTypes()[index];
		return type;
	}
	
	public <T> String[] determineAllowedSourceSets(Object focus, Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations) {
		for(SourceSetInvestigator investigator : configuration.getSourceSetInvestigators()) {
			String[] sets = investigator.determineAllowedSourceSets(focus, method, parameterClass, parameterName, parameterAnnotations, context);
			if(sets != null) {
				return sets;
			}
		}
		
		return null;
	}
	
	/**
	 * Determines which transformers should be attempted to run over the information returned from the data source
	 * before it is inject into the method being called.  All arguments are optional, but providing all of them
	 * is better.
	 * @param focus The object instance that method and argument belong to
	 * @param method The method that the argument belongs to
	 * @param parameterClass The class of the argument to the method being called.
	 * @param parameterName The name of the argument if known
	 * @param parameterAnnotations Any annotations for the argument
	 * @return
	 */
	public <T> TransformerSpecification[] determineTransformers(Object focus, Method method, Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations) {
		List<TransformerSpecification> allTransformers = new ArrayList<TransformerSpecification>();
		
		for(ParameterTransformersInvestigator investigator : configuration.getTransformersInvestigators()) {
			try {
				TransformerSpecification[] transformers = investigator.determineTransformers(focus, method, parameterClass, parameterName, parameterAnnotations, context);
				if(transformers != null) {
					addAll(allTransformers, transformers);
				}
			}
			catch(Exception e) {
				log.error("A transformer investigator failed while investigating parameter: " + parameterName, e);
			}
		}
		
		return allTransformers.toArray(new TransformerSpecification[allTransformers.size()]);
	}
	
	protected <T> void addAll(Collection<T> col, T[] ar) {
		for(T t : ar) {
			if(t != null) {
				col.add(t);
			}
		}
	}
	
	public Object getTypedParameter(Object focus, Method method, int argumentIndex) {
		
		Annotation[] parameterAnnotations = method.getParameterAnnotations()[argumentIndex];
		Class parameterClass = determineParameterClass(focus, method, argumentIndex);
		Type parameterType = determineParameterType(focus, method, argumentIndex);
		String parameterName = determineParameterName(focus, method, parameterClass, parameterAnnotations);
		String[] allowedSources = determineAllowedSourceSets(focus, method, parameterClass, parameterName, parameterAnnotations);
		TransformerSpecification[] transformers = determineTransformers(focus, method, parameterClass, parameterName, parameterAnnotations);
		
		Object initialData = getInitialObject(parameterName, parameterClass, allowedSources);
		return getTypedParameter(parameterName, parameterClass, getComponentClass(parameterType), initialData, transformers);
	}
	
	/**
	 * If the type is parameterized, return the first parameterized type.  This is useful for determining what the objects
	 * inside a collection should be typed as.
	 * @param type
	 * @return
	 */
	protected Class getComponentClass(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType)type;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class parameterArgClass = (Class) parameterArgType;
				return parameterArgClass;
			}
		}
		
		return null;
	}
	
	/**
	 * Takes <code>initial</code>, which is the data returned from the data source, and converts it using
	 * the specified transformers (<code>transformersToRun</code>) and then converts it if possible to
	 * the required final type that can be used as an argument to the call.
	 * @param name The name of the parameter being converted.  This is likely specified by an annotation
	 * @param finalType The type that the parameter must be in to use it as an argument to the method
	 * 			being called
	 * @param initial The object data we're starting with
	 * @param transformersToRun The named transformers that should be run over the data if possible
	 * @return
	 */
	public <T, O> T getTypedParameter(String name, Class<T> finalType, Class componentClass, O initial, TransformerSpecification ... transformersToRun) {
		 
		if(transformersToRun == null) {
			transformersToRun = new TransformerSpecification[0];
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
		
		Class componentType = getComponentType(finalType, componentClass);
		
		Class componentTypeAsArray = getArrayStyle(componentType);
		
		// If our type isn't right, let's convert it now
		if(isReturnTypeCompatible(startingPoint.getClass(), componentTypeAsArray) == false) {
			// We can't assign the current array to an array type for the parameter.  We'll have to convert
			startingPoint = makeArrayFromObject(convert(startingPoint, componentTypeAsArray));
		}
		
		// if they're are any transformers we haven't applied, maybe they needed to be applied after they were converted
		// so let's apply them now
		startingPoint = runTransformers(startingPoint, transformersRun, transformersToRun);
		
		
		if(isReturnTypeCompatible(startingPoint.getClass(), componentTypeAsArray) == false) {
			// we couldn't get converted and transformed to the destination data type
			log.error("Couldn't convert parameter " + name + " type " + initial.getClass().getName() + " to type " + 
			finalType.getName() + " and apply transformers: " + stringJoiner.transform(null, (Map<String, String>)null, null, getTransformerNames(transformersToRun)));
			return null;
		}
		
		return convertArrayToSingleValueOrCollectionIfNeeded(startingPoint, finalType);
	}
	
	protected Class getComponentType(Type type, Type passedComponentType) {
		if(type instanceof Class && Collection.class.isAssignableFrom((Class)type)) {
			if(type instanceof ParameterizedType) {
				Type t = ((ParameterizedType)type).getActualTypeArguments()[0];
				if(t instanceof Class) {
					return (Class)t;
				}
			}
			else if(passedComponentType != null) {
				return (Class)passedComponentType;
			}
			return Object.class;
		}
		return (Class)type;
	}
	protected String[] getTransformerNames(TransformerSpecification[] specs) {
		String[] names = new String[specs.length];
		for(int i = 0; i < specs.length; i++) {
			names[i] = specs[i].getTransformerName();
		}
		return names;
	}
	
	protected <T> T convertArrayToSingleValueOrCollectionIfNeeded(Object data, Class finalType) {
		if(data.getClass().isArray() && finalType.getClass().isArray() == false) {
			// we've been working with arrays, but we want a single value
			
			// let's try an automatic convert
			Object result = convert((Object[])data, finalType);
			if(isReturnTypeCompatible(result.getClass(), finalType)) {
				return (T)result;
			}
			
			if(Collection.class.isAssignableFrom(finalType)) {
				// we've got an array and the desired parameter is a collection of some sort
				// let's try to convert it to a collection
				return (T)arrayToCollection.transform(finalType, Object.class, (Object[])data);
			}
			else {
				// So, no auto convert, let's just hand back the first one then
				return (T)arrayToObject.transform(context, (Map<String,String>)null, null, (Object[])data);
			}
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
		for(ValueTransformer transformer : configuration.getTypeTransformers()) {
			if(canConvert(data, targetType, transformer)) {
				result = transformer.transform(null, (Map<String, String>)null, null, data);
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
	 * Runs <code>transformersToRun</code> if they can be run except those in 
	 * <code>transformersRun</code> on <code>data</code> adding those
	 * transforms run to the list as they're run.
	 * @param data The data to transform
	 * @param transformersRun The list of transformers that have been run already and should not be run again
	 * @param transformersToRun The transformers to run
	 * @return
	 */
	protected Object[] runTransformers(Object[] data, List<String> transformersRun, TransformerSpecification ... transformersToRun) {
		for(TransformerSpecification spec : transformersToRun) {
			if(transformersRun.contains(spec.getTransformerName())) {
				continue;
			}
			
			ValueTransformer transformer = configuration.getTransformers().get(spec.getTransformerName());
			if(transformer != null) {
				try {
					if(canRun(data, transformer)) {
						data = makeArrayFromObject(transformer.transform(null, spec.getTransformerProperties(), null, data));
						transformersRun.add(spec.getTransformerName());
					}
				}
				catch(TransformationException e) {
					log.error("Error transforming data with transformer: " + spec.getTransformerName(), e);
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
		
		Type argType = method.getGenericParameterTypes()[TRANSFORMER_SOURCE_DATA_ARG_POSITION];
		
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
		
		Type returnTypeType = method.getGenericReturnType();
		Class argClass = null;
		if(returnTypeType instanceof Class) {
			argClass = (Class)returnTypeType;
		}
		else if(returnTypeType instanceof ParameterizedType) {
			
		}
		else if(returnTypeType instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType)returnTypeType).getGenericComponentType();
			if(componentType instanceof Class) {
				argClass = getArrayStyle((Class)componentType);
			}
			else {
				Type genericClass = transformer.getClass().getGenericSuperclass();
				if(genericClass instanceof ParameterizedType) {
					argClass = getArrayStyle((Class)((ParameterizedType)genericClass).getActualTypeArguments()[0]);
				}
				else if(genericClass instanceof GenericArrayType) {
					@SuppressWarnings("unused")
					int i = 123;
				}
				else if(genericClass instanceof TypeVariable<?>) {
					@SuppressWarnings("unused")
					int i = 123;
				}
				else if(genericClass instanceof WildcardType) {
					@SuppressWarnings("unused")
					int i = 123;
				}
			}
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
	 * @param name The name of parameter (optional)
	 * @param type The type of the parameter (optional)
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

	public InvocationContext getContext() {
		return context;
	}

	public void setContext(InvocationContext context) {
		this.context = context;
	}

	public ParameterMarshalConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ParameterMarshalConfiguration configuration) {
		this.configuration = configuration;
	}
	
	
}
