package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.ParameterMarshal;

/**
 * A convenient way of managing the parameters passed by a request so they can be injected after the fact
 * of added to a location so that a user may edit their values if an error was found.
 * @author kolz
 *
 */
public class ParmManipulator {

	protected InvocationContext context;
	protected Logger log = SystemOutLogger.get(ParmManipulator.class);
	
	public ParmManipulator(InvocationContext context) {
		this.context = context;
	}
	
	/**
	 * Injects request parameters (and others if available) into the focus object.  Create for
	 * setting the request parameters after the fact.  It will not set a parameter marked with
	 * the {@link NoInject} annotation. 
	 * @param focus
	 */
	public void inject(Object focus) {
		inject(focus, (String[])null);
	}
	
	/**
	 * Injects request parameters (and others if available) into the focus object.  Create for
	 * setting the request parameters after the fact.  It will not set a parameter marked with
	 * the {@link NoInject} annotation. 
	 * @param focus
	 * @param parameterNames  This list of allowed properties to inject.
	 */
	public void inject(Object focus, String... parameterNames) {
		if(focus == null) {
			return;
		}
		
		ParameterMarshal marshal = context.getFoundParameter(ParameterMarshal.class);
		for(Method m : focus.getClass().getMethods()) {
			if(ReflectionUtils.isSetterMethod(m) == false) {
				continue;
			}
			
			if(ReflectionUtils.getAnnotation(m, NoInject.class) != null) {
				continue;
			}
			
			if(parameterNames != null && ReflectionUtils.contains(parameterNames, ReflectionUtils.getPropertyName(m)) == false) {
				// if there are allowed parameter names specified and this is not one of them, continue to the next parameter
				continue;
			}
			
			try {
				marshal.call(focus, m, false);
			}
			catch(Exception e) {
				log.error("Could not set property during inject for method: " + m.getName(), e);
			}
		}
	}
	
	/**
	 * Adds the parameters from the request to the location.
	 * @param location
	 */
	public void addRequestParameters(Location location) {
		addRequestParameters(location, (String[])null);
	}
	
	/**
	 * Adds the parameters from the request to the location.
	 * @param location
	 * @param allowedParameterNames  If not null, these are the only parameters that will be added
	 */
	public void addRequestParameters(Location location, String... allowedParameterNames) {
		HttpServletRequest request = context.getFoundParameter(HttpServletRequest.class);
		if(request == null) {
			return;
		}
		
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			if(allowedParameterNames != null && ReflectionUtils.contains(allowedParameterNames, name) == false) {
				// if there are allowed parameter names specified and this is not one of them, continue to the next parameter
				continue;
			}
			String[] values = request.getParameterValues(name);
			if(values.length == 0) {
				continue;
			}
			if(values.length == 1) {
				location.put(name, values[0]);
			}
			else {
				location.put(name, Arrays.asList(values));
			}
		}
	}
	
	/**
	 * Takes an application absolute url and prefixes the protocol, server, port, and context. 
	 * @param url
	 * @return 
	 */
	public String serverQualifyUrl(String url) {
		HttpServletRequest request = context.getFoundParameter(HttpServletRequest.class);
		if(request == null) {
			throw new NullPointerException("HttpServletRequest was null");
		}
		
		StringBuilder sb = new StringBuilder();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		sb.append(scheme);
		sb.append("://");
		sb.append(request.getServerName());
		if(shouldIncludePort(scheme, port)) {
			// So, we don't have a standard port here
			sb.append(":" + port);
		}
		
		sb.append(request.getContextPath());
		sb.append(url);
		return sb.toString();
	}
	
	/**
	 * Creates a realm identifier like the type needed for Open ID.  For example, if the server name is
	 * www.emergentideas.com, then the realm will be http://*.emergentideas.com
	 * @return
	 */
	public String getCurrentRealm() {
		HttpServletRequest request = context.getFoundParameter(HttpServletRequest.class);
		if(request == null) {
			throw new NullPointerException("HttpServletRequest was null");
		}
		
		String scheme = request.getScheme();
		int port = request.getServerPort();
		StringBuilder sb = new StringBuilder();
		sb.append(scheme);
		sb.append("://");
		
		boolean first = true;
		for(String part : request.getServerName().toLowerCase().split("\\.")) {
			if(first == false) {
				sb.append('.');
			}
			first = false;
			if("www".equals(part)) {
				sb.append("*");
			}
			else {
				sb.append(part);
			}
		}
		
		if(shouldIncludePort(scheme, port)) {
			// So, we don't have a standard port here
			sb.append(":" + port);
		}

		return sb.toString();
	}
	
	protected boolean shouldIncludePort(String scheme, int port) {
		return !(("https".equals(scheme) && port == 443) || ("http".equals(scheme) && port == 80));
	}
}
