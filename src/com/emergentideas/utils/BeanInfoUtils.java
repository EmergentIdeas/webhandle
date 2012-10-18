package com.emergentideas.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Id;

public class BeanInfoUtils {

	protected static final Class[] ALLOWED_PROPERTY_NAME_CLASSES = {
			String.class, Date.class, Number.class };

	/**
	 * Returns all the property names which are not an object (other than a
	 * simple type like String, Number, Date, etc.) and not marked with the Id
	 * annotation.
	 * 
	 * @param c
	 * @return
	 */
	public static List<String> determineNonIdNonObjectPropertyNames(Class<?> c) {
		List<String> result = new ArrayList<String>();

		determineRecursiveNonIdNonObjectPropertyNames(result, c);

		return result;
	}
	
	/**
	 * Finds the name of the id property which must also have a getter.  Otherwise returns
	 * null.
	 * @param c
	 * @return
	 */
	public static String determineIdPropertyNameWithGetter(Class<?> c) {
		String idProperty = null;
		Class<?> original = c;
		
		propertyLoop: while(idProperty == null) {
			for (Field f : c.getDeclaredFields()) {
				Annotation anno = f.getAnnotation(Id.class);
				if (anno != null) {
					idProperty = f.getName();
					break propertyLoop;
				}
			}
			
			Class<?> parent = c.getSuperclass();
			if(parent == null) {
				break;
			}

			c = parent;
		}
		
		if(idProperty != null) {
			Method m = ReflectionUtils.getGetterMethodFromClass(original, idProperty);
			if(m == null || ReflectionUtils.isPublic(m) == false) {
				idProperty = null;
			}
		}

		return idProperty;
	}
	
	public static List<String> determineNonIdNonObjectPropertyNamesWithGetters(Class<?> c) {
		List<String> properties = determineNonIdNonObjectPropertyNames(c);
		Iterator<String> it = properties.iterator();
		
		while(it.hasNext()) {
			String propertyName = it.next();
			Method m = ReflectionUtils.getGetterMethodFromClass(c, propertyName);
			if(m == null || ReflectionUtils.isPublic(m) == false) {
				it.remove();
			}
		}
		
		return properties;
	}
	
	public static List<String> formatCamelCasePropertyNames(List<String> propertyNames) {
		List<String> result = new ArrayList<String>();
		
		for(String propertyName : propertyNames) {
			result.add(formatCamelCasePropertyName(propertyName));
		}
		
		return result;
	}
	
	public static String formatCamelCasePropertyName(String propertyName) {
		boolean newWord = true;
		StringBuilder formatted = new StringBuilder();
		char[] chars = propertyName.toCharArray();
		for(char c : chars) {
			if(newWord) {
				formatted.append(Character.toUpperCase(c));
				newWord = false;
				continue;
			}
			
			if(Character.isUpperCase(c)) {
				formatted.append(' ');
			}
			
			formatted.append(c);
		}
		
		return formatted.toString();
	}

	protected static void determineRecursiveNonIdNonObjectPropertyNames(List<String> result, Class<?> c) {
		
		field: for (Field f : c.getDeclaredFields()) {
			Annotation anno = f.getAnnotation(Id.class);
			if (anno != null) {
				continue;
			}

			Class<?> fieldType = f.getType();
			if (ReflectionUtils.isPrimitive(fieldType)) {
				result.add(f.getName());
				continue;
			}
			if (ReflectionUtils.getPrimitiveType(fieldType) != null) {
				result.add(f.getName());
				continue;
			}
			for (Class allowed : ALLOWED_PROPERTY_NAME_CLASSES) {
				if (allowed.isAssignableFrom(fieldType)) {
					result.add(f.getName());
					continue field;
				}
			}
		}

		Class<?> parent = c.getSuperclass();
		if (parent != null && Object.class.equals(parent) == false) {
			determineRecursiveNonIdNonObjectPropertyNames(result, parent);
		}

	}

}
