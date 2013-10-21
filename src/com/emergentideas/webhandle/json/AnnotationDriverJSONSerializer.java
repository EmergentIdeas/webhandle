package com.emergentideas.webhandle.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class AnnotationDriverJSONSerializer implements Serializer {
	
	public static final String DEFAULT_PROFILE = "default";

	protected List<ObjectSerializer> allSerializers = Collections.synchronizedList(new ArrayList<ObjectSerializer>());
	
	/**
	 * Holds the compiled determination for which serializer is best to serialize a particular class
	 * for a particular profile. The key in the first map is the profile name. The key in the second
	 * map is the name of the class to be serialized. A key with a null value indicates that for this
	 * profile a search has been made but no appropriate serializer has been found.
	 */
	protected Map<String, Map<Class, ObjectSerializer>> compiledSerializationChoices = 
			Collections.synchronizedMap(new HashMap<String, Map<Class, ObjectSerializer>>());
	
	protected ObjectSerializer collectionSerializer = new CollectionSerializer();
	protected ObjectSerializer mapSerializer = new MapSerializer();
	protected ObjectSerializer arraySerializer = new ArraySerializer();
	
	
	@Override
	public void serialize(SegmentedOutput output, Object objToSerialize,
			String... allowedSerializationProfiles) throws Exception {
		if(objToSerialize == null) {
			output.getStream("body").append("null");
		}
		else if(objToSerialize instanceof Collection) {
			collectionSerializer.serialize(this, output, objToSerialize, allowedSerializationProfiles);
		}
		else if(objToSerialize instanceof Map) {
			mapSerializer.serialize(this, output, objToSerialize, allowedSerializationProfiles);
		}
		else if(objToSerialize.getClass().isArray()) {
			arraySerializer.serialize(this, output, objToSerialize, allowedSerializationProfiles);
		}
		else {
			ObjectSerializer os = determineSerializer(objToSerialize.getClass(), allowedSerializationProfiles);
			os.serialize(this, output, objToSerialize, allowedSerializationProfiles);
		}
		
	}
	public void add(ObjectSerializer serializer) {
		
		// Clear all of the compiled choices for any profile that this serializer
		// registers for
		for(String profile : findProfiles(serializer)) {
			getCompiledSerializersForProfile(profile).clear();
		}
		
		allSerializers.add(serializer);
	}
	protected Map<Class, ObjectSerializer> getCompiledSerializersForProfile(String profile) {
		synchronized (compiledSerializationChoices) {
			Map<Class, ObjectSerializer> m = compiledSerializationChoices.get(profile);
			if(m == null) {
				m = Collections.synchronizedMap(new HashMap<Class, ObjectSerializer>());
				compiledSerializationChoices.put(profile, m);
			}
			return m;
		}
	}
	
	protected boolean doesProfileHaveCompiledSerializer(String profile, Class classToSerialize) {
		return getCompiledSerializersForProfile(profile).containsKey(classToSerialize);
	}
	
	/**
	 * Determines which serializer to use based on the class to be serialized and the default profiles
	 * @param classToSerialize
	 * @param allowedProfiles The profiles that can be used to serialize the class in the order they
	 * can be used. Note: "default" will always be one of the options. If it is not explicitly listed
	 * it will be added as the last of the options.
	 * @return
	 */
	public ObjectSerializer determineSerializer(Class classToSerialize, String ... allowedProfiles) {
		if(allowedProfiles == null) {
			allowedProfiles = new String[] { DEFAULT_PROFILE };
		}
		else if(ReflectionUtils.contains(allowedProfiles, DEFAULT_PROFILE) == false) {
			String[] newProfiles = new String[allowedProfiles.length + 1];
			System.arraycopy(allowedProfiles, 0, newProfiles, 0, allowedProfiles.length);
			newProfiles[newProfiles.length - 1] = DEFAULT_PROFILE;
			allowedProfiles = newProfiles;
		}
		
		for(String profile : allowedProfiles) {
			ObjectSerializer s;
			if(doesProfileHaveCompiledSerializer(profile, classToSerialize)) {
				s = getCompiledSerializersForProfile(profile).get(classToSerialize);
			}
			else {
				s = determineBestSerializer(classToSerialize, profile);
				getCompiledSerializersForProfile(profile).put(classToSerialize, s);
			}
			if(s != null) {
				return s;
			}
		}
		
		return null;
	}
	
	protected ObjectSerializer determineBestSerializer(Class classToSerialize, String profile) {
		List<ObjectSerializer> all = new ArrayList<ObjectSerializer>(allSerializers);
		
		int maxDistance = Integer.MAX_VALUE;
		ObjectSerializer lastFound = null;
		
		for(ObjectSerializer focus : all) {
			if(ReflectionUtils.contains(findProfiles(focus), profile) == false) {
				continue;
			}
			Integer i = ReflectionUtils.findDirectionalClassDistance(findTypeSerialized(focus), classToSerialize);
			if(i != null && i >= 0) {
				if(i <= maxDistance) {
					maxDistance = i;
					lastFound = focus;
				}
			}
		}
		
		return lastFound;
	}
	
	protected String[] findProfiles(ObjectSerializer focus) {
		JSONSerializer anno = ReflectionUtils.getAnnotationOnClass(focus.getClass(), JSONSerializer.class);
		return anno.value();
	}
	
	protected Class findTypeSerialized(Object focus) {
		Method m = ReflectionUtils.getFirstMethod(focus.getClass(), "serialize");
		if(m == null) {
			return null;
		}
		
		return m.getParameterTypes()[2];
	}
}
