package com.emergentideas.webhandle.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.output.Respondent;

public class HandleCaller {
	
	public static final String EXCEPTION_PARAMETER_NAME = "exception";
	
	protected HandlerInvestigator handlerInvestigator;
	protected OutputResponseInvestigator outputInvestigator;
	
	protected Map<Class, CallSpec> exceptionHandlers = Collections.synchronizedMap(new HashMap<Class, CallSpec>());
	
	protected Logger log = SystemOutLogger.get(HandleCaller.class);
	
	public void call(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ParameterMarshal marshal) {
		
		CallSpec called = null;
		Object result = null;
		
		try {
			for(CallSpec spec : handlerInvestigator.determineHandlers(getUrl(request), getMethod(request))) {
				try {
					result = callAndUnwrapException(marshal, spec);
					called = spec;
					break;
				}
				catch(CouldNotHandleException ex) {
					// Probably doesn't even need to be logged, just one of the handlers saying they couldn't actually
					// handle the request
				}
			}
		}
		catch(Exception e) {
			marshal.getContext().setFoundParameter(EXCEPTION_PARAMETER_NAME, Exception.class, e);
			
			for(Class c : exceptionHandlers.keySet()) {
				if(c.isAssignableFrom(e.getClass())) {
					try {
						result = callAndUnwrapException(marshal, exceptionHandlers.get(c));
						called = exceptionHandlers.get(c);
						break;
					}
					catch(Exception ex) {
						// so, we're probably f'd
						log.error("Problem executing an exception handler for exception type: " + c.getName(), ex);
					}
				}
			}
		}
		
		if(called != null) {
			Respondent resp = outputInvestigator.determineTransformers(marshal.getContext(), called.getFocus(), called.getMethod(), result);
			if(resp != null) {
				resp.respond(servletContext, request, response);
			}
		}
	}
	
	protected Object callAndUnwrapException(ParameterMarshal marshal, CallSpec spec) throws InvocationTargetException, IllegalAccessException {
		try {
			return marshal.call(spec);
		}
		catch(InvocationTargetException e) {
			if(e.getCause() instanceof RuntimeException) {
				throw (RuntimeException)e.getCause();
			}
			throw e;
		}
	}


	public Map<Class, CallSpec> getExceptionHandlers() {
		return exceptionHandlers;
	}

	protected String getUrl(HttpServletRequest request) {
		String requestPath = request.getServletPath();
		if(requestPath == null || "".equals(requestPath))
		{
			requestPath = request.getRequestURI();
			requestPath = requestPath.substring(request.getContextPath().length());
		}
		return requestPath;
	}
	
	protected HttpMethod getMethod(HttpServletRequest request) {
		HttpMethod method = HttpMethod.valueOf(request.getMethod().toUpperCase());
		if(method == null) {
			method = HttpMethod.UNKNOWN;
		}
		
		return method;
	}


	public HandlerInvestigator getHandlerInvestigator() {
		return handlerInvestigator;
	}


	public void setHandlerInvestigator(HandlerInvestigator handlerInvestigator) {
		this.handlerInvestigator = handlerInvestigator;
	}


	public OutputResponseInvestigator getOutputInvestigator() {
		return outputInvestigator;
	}


	public void setOutputInvestigator(OutputResponseInvestigator outputInvestigator) {
		this.outputInvestigator = outputInvestigator;
	}
	
	
}
