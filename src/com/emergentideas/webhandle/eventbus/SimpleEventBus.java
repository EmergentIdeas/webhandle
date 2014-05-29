package com.emergentideas.webhandle.eventbus;

import static com.emergentideas.utils.ReflectionUtils.determineParameterizedArgumentType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.handlers.UrlRegexOutput;
import com.emergentideas.webhandle.handlers.UrlRequestElementsProcessor;

@Resource(type = EventBus.class)
public class SimpleEventBus implements EventBus {

	protected UrlRequestElementsProcessor urlProcessor = new UrlRequestElementsProcessor();
	
	protected Map<UrlRegexOutput, CallSpec> listenerCalls = Collections.synchronizedMap(new HashMap<UrlRegexOutput, CallSpec>());
	
	protected ParameterMarshal parameterMarshal = new ParameterMarshal(new IntegratorConfiguration());
	
	@Override
	public void register(Object listener) {
		Map<String, Method> methodMap = createMethodMap(ReflectionUtils.getMethodsInReverseInheritenceOrder(listener.getClass()));
		for(String url : methodMap.keySet()) {
			Method m = methodMap.get(url);
			UrlRegexOutput regex = urlProcessor.process(url);
			CallSpec spec = new CallSpec();
			spec.setFocus(listener);
			spec.setMethod(m);
			listenerCalls.put(regex, spec);
		}
	}
	
	
	
	@Override
	public void unregister(Object listener) {
		Iterator<Map.Entry<UrlRegexOutput,CallSpec>> it = listenerCalls.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<UrlRegexOutput,CallSpec> entry = it.next();
			if(entry.getValue().getFocus().equals(listener)) {
				it.remove();
			}
		}
		
	}



	/**
	 * The methods marked with an {@link EventListener} annotation with raw path as
	 * the key to the table
	 */
	protected Map<String, Method> createMethodMap(List<Method> methods) {
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for(Method m : methods) {
			EventListener e = ReflectionUtils.getAnnotation(m, EventListener.class);
			if(e != null) {
				for(String url : e.value()) {
					methodMap.put(url, m);
				}
			}
		}
		return methodMap;
	}

	@Override
	public void emit(String queue, Object event) {
		Set<Object> triggered = new HashSet<Object>();
		
		for(UrlRegexOutput regex : listenerCalls.keySet()) {
			Map<String, Object> parameters = (Map)regex.matches(queue);
			if(parameters != null) {
				CallSpec spec = listenerCalls.get(regex);
				if(triggered.contains(spec.getFocus())) {
					continue;
				}
				try {
					if(event != null) {
						
						Class parameterClass = determineFirstParameterType(spec);
						if(parameterClass != null) {
							if(parameterClass.isAssignableFrom(event.getClass())) {
								String parmName = determineFirstParameterName(spec, parameterClass);
								parameters.put(parmName, event);
							}
							else {
								continue;
							}
						}
					}

					parameterMarshal.call(spec.getFocus(), spec.getMethod(), spec.isFailOnMissingParameter(), Constants.USER_INFORMATION_SOURCE_NAME, parameters);
					triggered.add(spec.getFocus());
				}
				catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		
	}

	protected Class determineFirstParameterType(CallSpec spec) {
		if(spec.getMethod().getParameterTypes().length > 0) {
			Class parameterClass = parameterMarshal.determineParameterClass(spec.getFocus(), spec.getMethod(), 0);
			Type parameterType = parameterMarshal.determineParameterType(spec.getFocus(), spec.getMethod(), 0);
			
			if(parameterType != null && parameterType instanceof TypeVariable) {
				Class<?> c = determineParameterizedArgumentType(parameterType, spec.getFocus());
				if(c != null) {
					parameterClass = c;
				}
			}
			
			return parameterClass;
		}
		
		return null;
	}
	
	protected String determineFirstParameterName(CallSpec spec, Class parameterClass) {
		String parmName = parameterMarshal.determineParameterName(spec.getFocus(), spec.getMethod(), parameterClass, 
				parameterMarshal.getConfiguration().getParameterAnnotations(spec.getMethod())[0], 0);
		return parmName;
	}
	
}
