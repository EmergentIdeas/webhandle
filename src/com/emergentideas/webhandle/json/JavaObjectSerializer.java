package com.emergentideas.webhandle.json;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class JavaObjectSerializer implements ObjectSerializer<Object> {

	protected MapSerializer mapSerializer = new MapSerializer();
	protected EnumSerializer enumSerializer = new EnumSerializer();
	
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Object objToSerialize, String... allowedSerializationProfiles) throws Exception {
		
		if(objToSerialize instanceof Enum) {
			enumSerializer.serialize(callingSerializer, output, (Enum)objToSerialize, allowedSerializationProfiles);
			return;
		}
		
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		
		BeanInfo info = Introspector.getBeanInfo(objToSerialize.getClass());
		for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method m = pd.getReadMethod();
			if(m == null) {
				continue;
			}
			if(ReflectionUtils.isReturnTypeVoid(m)) {
				continue;
			}
			String name = pd.getName();
			Class returnType = pd.getReadMethod().getReturnType();
			Type parameterizedType = pd.getReadMethod().getGenericReturnType();
			Object value = pd.getReadMethod().invoke(objToSerialize);
			if(isValueSimpleEnough(name, value, returnType, parameterizedType)) {
				values.put(name, value);
			}
		}
		
		values = addAndRemoveProperties(objToSerialize, values);
		mapSerializer.serialize(callingSerializer, output, values, allowedSerializationProfiles);
	}
	
	/**
	 * Allows overriders to add or remove data from the map which will be serialized. If desired,
	 * an entirely new map can be returned.
	 * @param objToSerialize
	 * @param values
	 * @return
	 */
	protected Map<String, Object> addAndRemoveProperties(Object objToSerialize, Map<String, Object> values) {
		return values;
	}
	
	protected boolean isValueSimpleEnough(String propertyName, Object obj, Class returnType, Type parameterType) {
		return isSimpleEnoughType(obj, returnType, parameterType);
	}
	
	/**
	 * Returns true if the object is a primitive type, an Object version of a primitive type,
	 * a string, an array of the aforementioned types,
	 * a collection that can contain only the aforementioned items, or a map which
	 * has string keys and values which can only be the aforementioned types.
	 * @param returnType The return type as declared by the method
	 * @param parameterType The parameterized return type as declared by the method.
	 * @return
	 */
	protected boolean isSimpleEnoughType(Object obj, Class returnType, Type parameterType) {
		if(returnType == null) {
			return false;
		}
		if(ReflectionUtils.isPrimitive(returnType)) {
			return true;
		}
		if(ReflectionUtils.getPrimitiveType(returnType) != null) {
			return true;
		}
		if(returnType.equals(String.class)) {
			return true;
		}
		if(returnType.isArray()) {
			return isSimpleEnoughType(null, returnType.getComponentType(), null);
		}
		if(Date.class.isAssignableFrom(returnType)) {
			return true;
		}
		if(Calendar.class.isAssignableFrom(returnType)) {
			return true;
		}
		if(Enum.class.isAssignableFrom(returnType)) {
			return true;
		}
		
		
		if(Collection.class.isAssignableFrom(returnType) && obj != null) {
			Collection c = (Collection)obj;
			for(Object o : c) {
				if(isSimpleEnoughType(o, o.getClass(), null) == false) {
					return false;
				}
			}
			return true;
		}
		if(Map.class.isAssignableFrom(returnType) && obj != null) {
			Map m = (Map)obj;
			for(Object o : m.keySet()) {
				if(o instanceof String == false) {
					return false;
				}
				Object value = m.get(o);
				if(value == null) {
					continue;
				}
				if(isSimpleEnoughType(value, value.getClass(), null) == false) {
					return false;
				}
			}
			return true;
		}
		
		
		return false;

	}

	
}
