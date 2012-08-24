package com.emergentideas.webhandle.handlers;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;

/**
 * Examines a bunch of objects and returns a call spec for the best one to handle this url if
 * any of them will handle a this url.
 * @author kolz
 *
 */
public class HandleAnnotationHandlerInvestigator implements HandlerInvestigator {

	protected UrlRequestElementsProcessor processor = new UrlRequestElementsProcessor();
	
	protected List<UrlRegexOutput> handlerRegexs = Collections.synchronizedList(new ArrayList<UrlRegexOutput>());
	
	protected Map<UrlRegexOutput, HttpRequestCallSpec> handlerCalls = Collections.synchronizedMap(new HashMap<UrlRegexOutput, HttpRequestCallSpec>());
	
	public void analyzeObject(Object handler) {
		String[] urlPrefixPattern;
		
		Handle handle = ReflectionUtils.getAnnotationOnClass(handler.getClass(), Handle.class);
		if(handle != null) {
			urlPrefixPattern = handle.path();
		}
		else {
			urlPrefixPattern = new String[] { "" };
		}
		
		for(Method method : handler.getClass().getMethods()) {
			handle = ReflectionUtils.getAnnotation(method, Handle.class);
			if(ReflectionUtils.isPublic(method) == false) {
				continue;
			}
			if(handle == null) {
				continue;
			}
			
			for(String prefix : urlPrefixPattern) {
				for(String suffix : handle.path()) {
					UrlRegexOutput regex = processor.process(prefix + suffix);
					HttpRequestCallSpec spec = new HttpRequestCallSpec();
					spec.setFocus(handler);
					spec.setMethod(method);
					spec.setAllowedMethods(handle.method());
					handlerRegexs.add(regex);
					handlerCalls.put(regex, spec);
				}
			}
		}
	}
	
	
	public CallSpec determineHandler(String requestUrl, HttpMethod method) {
		for(UrlRegexOutput regex : handlerRegexs) {
			Map<String, String> properties = regex.matches(requestUrl);
			if(properties != null) {
				HttpRequestCallSpec spec = handlerCalls.get(regex);
				if(contains(spec.getAllowedMethods(), method)) {
					// looks like we've found a method which will handle the url
					// and the request type.
					CallSpec userSpec =  new CallSpec(spec.getFocus(), spec.getMethod(), false);
					userSpec.setCallSpecificProperties(properties);
					return userSpec;
				}
			}
		}
		
		return null;
	}
	
	protected <T> boolean contains(T[] baseSet, T focus) {
		for(T t : baseSet) {
			if(t == null && focus == null) {
				return true;
			}
			if(t == null || focus == null) {
				continue;
			}
			
			if(t == focus) {
				return true;
			}
			
			if(t.equals(focus)) {
				return true;
			}
		}
		
		return false;
	}

}