package com.emergentideas.webhandle.handlers;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

/**
 * Examines a bunch of objects and returns a call spec for the best one to handle this url if
 * any of them will handle a this url.  It also serves as its own integrator, detecting new
 * beans with Handle annotations as they're loaded.
 * @author kolz
 *
 */
@Type("com.emergentideas.webhandle.handlers.HandlerInvestigator")
@Integrate
public class HandleAnnotationHandlerInvestigator implements HandlerInvestigator, Integrator {

	protected UrlRequestElementsProcessor processor = new UrlRequestElementsProcessor();
	
	protected List<UrlRegexOutput> handlerRegexs = Collections.synchronizedList(new ArrayList<UrlRegexOutput>());
	
	protected Map<UrlRegexOutput, HttpRequestCallSpec> handlerCalls = Collections.synchronizedMap(new HashMap<UrlRegexOutput, HttpRequestCallSpec>());
	
	protected String characterEncoding = Constants.DEFAULT_CHARACTER_ENCODING;
	
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus != null) {
			analyzeObject(focus);
		}
	}


	public void analyzeObject(Object handler) {
		String[] urlPrefixPattern = null;
		
		Handle handle = ReflectionUtils.getAnnotationOnClass(handler.getClass(), Handle.class);
		Path path = ReflectionUtils.getAnnotationOnClass(handler.getClass(), Path.class);
		
		if(handle != null) {
			urlPrefixPattern = handle.value();
		}
		else if(path != null) {
			urlPrefixPattern = new String[] { path.value() };
		}
		
		if(urlPrefixPattern == null) {
			urlPrefixPattern = new String[] { "" };
		}
		
		for(Method method : removeDuplicates(getMethodsInReverseInheritenceOrder(handler.getClass()), handle, path, urlPrefixPattern)) {
			if(ReflectionUtils.isPublic(method) == false) {
				continue;
			}
			
			handle = ReflectionUtils.getAnnotation(method, Handle.class);
			path = ReflectionUtils.getAnnotation(method, Path.class);
			if(handle == null && path == null) {
				continue;
			}
			
			List<String> values = new ArrayList<String>();
			if(handle != null) {
				for(String s : handle.value()) {
					values.add(s);
				}
			}
			else if(path != null) {
				values.add(path.value());
			}
			
			for(String prefix : urlPrefixPattern) {
				
				HttpMethod[] allowedMethods = extractMethods(method);
				
				for(String suffix : values) {
					String url = prefix + suffix;
					addHandlerToLists(handler, method, allowedMethods, url);
				}
			}
		}
	}
	
	/**
	 * Removes the first of any url and method combo from the list of methods
	 * @param methods
	 * @return
	 */
	protected List<Method> removeDuplicates(List<Method> methods, Handle handle, Path path, String[] urlPrefixPattern) {
		
		List<Method> methodsToRemove = new ArrayList<Method>();
		Map<String, Method> definedUrlMethodCombos = new HashMap<String, Method>();
		
		for(Method method : methods) {
			if(ReflectionUtils.isPublic(method) == false) {
				continue;
			}
			
			handle = ReflectionUtils.getAnnotation(method, Handle.class);
			path = ReflectionUtils.getAnnotation(method, Path.class);
			if(handle == null && path == null) {
				continue;
			}
			
			List<String> values = new ArrayList<String>();
			if(handle != null) {
				for(String s : handle.value()) {
					values.add(s);
				}
			}
			else if(path != null) {
				values.add(path.value());
			}
			
			for(String prefix : urlPrefixPattern) {
				HttpMethod[] allowedMethods = extractMethods(method);
				for(String suffix : values) {
					String url = prefix + suffix;
					String urlAndMethod = url + createHttpMethodsString(allowedMethods);
					if(definedUrlMethodCombos.containsKey(urlAndMethod)) {
						methodsToRemove.add(definedUrlMethodCombos.get(urlAndMethod));
					}
					definedUrlMethodCombos.put(urlAndMethod, method);
				}
			}
		}
		
		for(Method m : methodsToRemove) {
			methods.remove(m);
		}
		
		return methods;
	}
	
	protected void addHandlerToLists(Object handler, Method method, HttpMethod[] allowedMethods, String url) {
		UrlRegexOutput regex = processor.process(url);
		HttpRequestCallSpec spec = new HttpRequestCallSpec();
		spec.setFocus(handler);
		spec.setMethod(method);
		spec.setAllowedMethods(allowedMethods);
		handlerRegexs.add(0, regex);
		handlerCalls.put(regex, spec);
	}
	
	protected HttpMethod[] extractMethods(Method m) {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		
		Handle handle = ReflectionUtils.getAnnotation(m, Handle.class);
		if(handle != null) {
			return handle.method();
		}
		else {
			addIfPresent(m, GET.class, methods, HttpMethod.GET);
			addIfPresent(m, POST.class, methods, HttpMethod.POST);
			addIfPresent(m, PUT.class, methods, HttpMethod.PUT);
			addIfPresent(m, DELETE.class, methods, HttpMethod.DELETE);
			addIfPresent(m, OPTIONS.class, methods, HttpMethod.OPTIONS);
			addIfPresent(m, HEAD.class, methods, HttpMethod.HEAD);
			
			if(methods.size() == 0) {
				methods.add(HttpMethod.ANY);
			}
		}
		
		return methods.toArray(new HttpMethod[methods.size()]);
	}
	
	protected void addIfPresent(Method m, Class annotationClass, List<HttpMethod> methods, HttpMethod httpMethod) {
		if(ReflectionUtils.getAnnotation(m, annotationClass) != null) {
			methods.add(httpMethod);
		}
	}
	
	protected String createHttpMethodsString(HttpMethod[] methods) {
		List<String> list = new ArrayList<String>();
		for(HttpMethod m : methods) {
			list.add(m.name());
		}
		
		Collections.sort(list);
		return StringUtils.join(list, ',');
	}
	
	/**
	 * Returns the method method of a class with the one from the actual type coming first,
	 * then the methods from the super class, then its superclass, etc.
	 * @param c
	 * @return
	 */
	protected List<Method> getMethodsInReverseInheritenceOrder(Class<?> c) {
		List<Method> result = new ArrayList<Method>();
		
		while(c != null) {
			for(Method m : c.getDeclaredMethods()) {
				result.add(m);
			}
			c = c.getSuperclass();
		}
		
		Collections.reverse(result);
		return result;
	}
	
	
	public CallSpec[] determineHandlers(String requestUrl, HttpMethod method) {
		
		List<CallSpec> result = new ArrayList<CallSpec>();
		
		for(UrlRegexOutput regex : handlerRegexs) {
			Map<String, String> properties = regex.matches(requestUrl);
			if(properties != null) {
				urlDecode(properties);
				HttpRequestCallSpec spec = handlerCalls.get(regex);
				if(contains(spec.getAllowedMethods(), HttpMethod.ANY) || contains(spec.getAllowedMethods(), method)) {
					// looks like we've found a method which will handle the url
					// and the request type.
					CallSpec userSpec =  new CallSpec(spec.getFocus(), spec.getMethod(), false);
					userSpec.setCallSpecificProperties(properties);
					result.add(userSpec);
				}
			}
		}
		
		return result.toArray(new CallSpec[result.size()]);
	}
	
	protected void urlDecode(Map<String, String> properties) {
		for(Map.Entry<String, String> entry : properties.entrySet()) {
			try {
				entry.setValue(URLDecoder.decode(entry.getValue(), characterEncoding));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
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
