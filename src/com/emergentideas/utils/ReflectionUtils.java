package com.emergentideas.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
	
	protected static Class[] primitives = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE };

	
	/**
	 * Returns true if the method follows the rules to be a setter on a bean.
	 * @param method
	 * @return
	 */
	public static boolean isSetterMethod(Method method) {
		String methodName = method.getName();
		
		if(methodName.startsWith("set") && methodName.length() > 3 && Character.isUpperCase(methodName.charAt(3)) && method.getParameterTypes().length == 1) {
			return true;
		}

		return false;
	}
	
	/**
	 * Returns the property name if this is a setter method or null otherwise.
	 * @param method
	 * @return
	 */
	public static String getPropertyName(Method method) {
		
		if(isSetterMethod(method)) {
			String methodName = method.getName();
			String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
			return propertyName;
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
	public static Class getArrayStyle(Class type) {
		if(isArrayStyle(type)) {
			// Okay, it is an array, so just return it
			return type;
		}
		return Array.newInstance(type, 0).getClass();
	}
	
	/**
	 * Returns the component type if the class is an array class or <code>type</code> otherwise.
	 * @param type
	 * @return
	 */
	public static Class getNonArrayStyle(Class type) {
		if(isArrayStyle(type)) {
			return type.getComponentType();
		}
		return type;
	}
	
	/**
	 * Makes a single object into an array of those objects (length 1) if it is not already an array. If <code>o</code>
	 * is null, then return null.
	 * @param o
	 * @return
	 */
	public static <O> O[] makeArrayFromObject(O o) {
		if(o == null) {
			return null;
		}
		if(o.getClass().isArray()) {
			return (O[])o;
		}
		
		O[] result = (O[])Array.newInstance(o.getClass(), 1);
		result[0] = o;
		return result;
	}
	
	/**
	 * Returns true if this class is an array type of some sort.
	 * @param type
	 * @return
	 */
	public static boolean isArrayStyle(Class type) {
		if(type.getName().startsWith("[")) {
			return true;
		}
		return false;
	}
	
	/**
	 * returns true if the object is an array of something
	 * @param o
	 * @return
	 */
	public static boolean isArrayType(Object o) {
		return o.getClass().isArray();
	}
	
	/**
	 * Returns the annotation of the specified type if it exists on the method.
	 * @param m
	 * @param type
	 * @return
	 */
	public static <T> T getAnnotation(Method m, Class<T> type) {
		for(Annotation anno : m.getAnnotations()) {
			if(type.isAssignableFrom(anno.getClass())) {
				return (T)anno;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the annotation of the specified type if it exists on the class itself.
	 * @param m
	 * @param type
	 * @return
	 */
	public static <T> T getAnnotationOnClass(Class c, Class<T> type) {
		for(Annotation anno : c.getAnnotations()) {
			if(type.isAssignableFrom(anno.getClass())) {
				return (T)anno;
			}
		}
		
		return null;
	}

	
	/**
	 * Returns true if an annotation of the specified class exists on the method.
	 * @param m
	 * @param type
	 * @return
	 */
	public static <T> boolean hasAnnotation(Method m, Class<T> type) {
		for(Annotation anno : m.getAnnotations()) {
			if(type.isAssignableFrom(anno.getClass())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if <code>type</code> is the class for a primitive type like int or float.
	 * @param type
	 * @return
	 */
    public static boolean isPrimitive(Class type) {
    	for(Class c : primitives) {
    		if(c == type) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    /**
     * Returns the primitive type for a class like Integer which has a corresponding primitive. 
     * @param type
     * @return
     */
    public static Class getPrimitiveType(Class type) {
        if (type == Boolean.class)
        	return Boolean.TYPE;
        if (type == Byte.class)
        	return Byte.TYPE;
        if (type == Character.class)
        	return Character.TYPE;
        if (type == Short.class)
        	return Short.TYPE;
        if (type == Integer.class)
        	return Integer.TYPE;
        if (type == Long.class)
        	return Long.TYPE;
        if (type == Float.class)
        	return Float.TYPE;
        if (type == Double.class)
        	return Double.TYPE;
        if (type == Void.class)
        	return Void.TYPE;
        return null;
    }
    
    /**
     * returns the default value for a primitive type as a not primitive object.  So,
     * if Integer.TYPE is passed then the return value will be like new Integer(0).
     * @param type
     * @return
     */
    public static <T> Object getDefault(Class<T> type) {
        if (type == Boolean.TYPE)
        	return Boolean.valueOf(false);
        if (type == Byte.TYPE)
        	return Byte.valueOf((byte)0);
        if (type == Character.TYPE)
        	return Character.valueOf((char)0);
        if (type == Short.TYPE)
        	return Short.valueOf((short)0);
        if (type == Integer.TYPE)
        	return Integer.valueOf(0);
        if (type == Long.TYPE)
        	return Long.valueOf(0);
        if (type == Float.TYPE)
        	return Float.valueOf(0);
        if (type == Double.TYPE)
        	return Double.valueOf(0);
        return null;
    }


    /**
     * Gets the first method from <code>focus</code> of the name <code>methodName</code>. Returns null
     * if no method of that name is found.
     * @param focus
     * @param methodName
     * @return
     */
	public static <T> Method getFirstMethod(Class<T> focus, String methodName) {
		for(Method m : focus.getMethods()) {
			if(m.getName().equals(methodName)) {
				return m;
			}
		}
		
		return null;
	}
	
	public static boolean isPublic(Method m) {
		return Modifier.isPublic(m.getDeclaringClass().getModifiers());
	}
}
