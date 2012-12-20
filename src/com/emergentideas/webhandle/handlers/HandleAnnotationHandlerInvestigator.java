package com.emergentideas.webhandle.handlers;


import java.lang.reflect.Method;
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
	
	
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus != null) {
			analyzeObject(focus);
		}
	}


	public void analyzeObject(Object handler) {
		String[] urlPrefixPattern = null;
		
		Set<String> definedUrlMethodCombos = new HashSet<String>(); 
		
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
		
		for(Method method : getMethodsInInheritenceOrder(handler.getClass())) {
			if(ReflectionUtils.isPublic(method) == false) {
				continue;
			}
			
			handle = ReflectionUtils.getAnnotation(method, Handle.class);
			path = ReflectionUtils.getAnnotation(method, Path.class);
			if(handle == null && path == null) {
				continue;
			}
			
			for(String prefix : urlPrefixPattern) {
				if(handle != null) {
					for(String suffix : handle.value()) {
						String url = prefix + suffix;
						String urlAndMethod = url + createHttpMethodsString(handle.method());
						if(definedUrlMethodCombos.contains(urlAndMethod)) {
							continue;
						}
						definedUrlMethodCombos.add(urlAndMethod);
						addHandlerToLists(handler, method, handle.method(), url);
					}
				}
				else if(path != null) {
					String url = prefix + path.value();
					HttpMethod[] allowedMethods = extractMethods(method);
					String urlAndMethod = url + createHttpMethodsString(allowedMethods);
					if(definedUrlMethodCombos.contains(urlAndMethod)) {
						continue;
					}
					definedUrlMethodCombos.add(urlAndMethod);
					addHandlerToLists(handler, method, allowedMethods, url);
				}
			}
		}
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
		addIfPresent(m, GET.class, methods, HttpMethod.GET);
		addIfPresent(m, POST.class, methods, HttpMethod.POST);
		addIfPresent(m, PUT.class, methods, HttpMethod.PUT);
		addIfPresent(m, DELETE.class, methods, HttpMethod.DELETE);
		addIfPresent(m, OPTIONS.class, methods, HttpMethod.OPTIONS);
		addIfPresent(m, HEAD.class, methods, HttpMethod.HEAD);
		
		if(methods.size() == 0) {
			methods.add(HttpMethod.ANY);
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
	protected List<Method> getMethodsInInheritenceOrder(Class<?> c) {
		List<Method> result = new ArrayList<Method>();
		
		while(c != null) {
			for(Method m : c.getDeclaredMethods()) {
				result.add(m);
			}
			c = c.getSuperclass();
		}
		
		return result;
	}
	
	
	public CallSpec[] determineHandlers(String requestUrl, HttpMethod method) {
		
		List<CallSpec> result = new ArrayList<CallSpec>();
		
		for(UrlRegexOutput regex : handlerRegexs) {
			Map<String, String> properties = regex.matches(requestUrl);
			if(properties != null) {
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
