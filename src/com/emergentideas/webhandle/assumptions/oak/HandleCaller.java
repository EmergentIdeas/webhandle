package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.configurations.WebRequestContextPopulator;
import com.emergentideas.webhandle.exceptions.CouldNotHandle;
import com.emergentideas.webhandle.exceptions.CouldNotHandleException;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.handlers.ResponseLifecycleHandler;
import com.emergentideas.webhandle.output.Respondent;

@Name("request-handler")
@Type("com.emergentideas.webhandle.handlers.ResponseLifecycleHandler")
public class HandleCaller implements ResponseLifecycleHandler {
	
	
	public static final String EXCEPTION_PARAMETER_NAME = "exception";
	
	protected HandlerInvestigator handlerInvestigator;
	protected OutputResponseInvestigator outputInvestigator;
	
	protected Map<Class, CallSpec> exceptionHandlers = Collections.synchronizedMap(new HashMap<Class, CallSpec>());
	
	protected List<CallSpec> preHandleCalls = Collections.synchronizedList(new ArrayList<CallSpec>());
	protected List<CallSpec> preResponseCalls = Collections.synchronizedList(new ArrayList<CallSpec>());
	protected List<CallSpec> postResponseCalls = Collections.synchronizedList(new ArrayList<CallSpec>());
	protected List<CallSpec> normalHanlderFailedCalls = Collections.synchronizedList(new ArrayList<CallSpec>());
	
	// The app level location
	protected Location location;
	
	protected Logger log = SystemOutLogger.get(HandleCaller.class);
	
	protected String handlerIdentifier = UUID.randomUUID().toString();
	protected String handlerLocationIdentifier = handlerIdentifier + ".location";
	
	protected WebRequestContextPopulator populator = new WebRequestContextPopulator();

	
	public void respond(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		setupUserSession(request, response);
		Location userLocation = (Location)request.getSession().getAttribute(handlerLocationIdentifier);
		
		WebAppLocation webApp = new WebAppLocation(userLocation);
		ParameterMarshalConfiguration conf = (ParameterMarshalConfiguration)webApp.getServiceByName(WebAppLocation.WEB_PARAMETER_MARSHAL_CONFIGURATION);
		ParameterMarshal marshal = new ParameterMarshal(conf);
		marshal.getContext().setLocation(userLocation);
		marshal.getContext().setFoundParameter(HttpServletRequest.class, request);
		marshal.getContext().setFoundParameter(HttpServletResponse.class, response);
		marshal.getContext().setFoundParameter(ServletContext.class, servletContext);
		
		populator.populate(marshal, marshal.getContext(), request);
		
		call(servletContext, request, response, marshal);
	}
	
	protected void setupUserSession(HttpServletRequest request, HttpServletResponse response) {
		Location loc = (Location)request.getSession().getAttribute(handlerLocationIdentifier);
		if(loc == null) {
			loc = new AppLocation(location);
			loc.put(Constants.SESSION_LOCATION, loc);
			request.getSession().setAttribute(handlerLocationIdentifier, loc);
		}
	}

	public void call(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ParameterMarshal marshal) {
		
		CallSpec called = null;
		Object result = null;
		
		try {
			
			// Call all the methods we'll need before the actual handler is called
			for(CallSpec spec : preHandleCalls) {
				result = callAndUnwrapException(marshal, spec);
				called = spec;
			}
			
			for(CallSpec spec : handlerInvestigator.determineHandlers(getUrl(request), getMethod(request))) {
				try {
					result = callAndUnwrapException(marshal, spec);
					if(result != null && result instanceof CouldNotHandle) {
						// We can either catch the exception or handle it as a returned value.  Either way, it just
						// means that even though the handler was registered to handle these type of URLs, it couldn't,
						// and another handler should get a crack at it.
						continue;
					}
					called = spec;
					break;
				}
				catch(CouldNotHandleException ex) {
					// Probably doesn't even need to be logged, just one of the handlers saying they couldn't actually
					// handle the request
				}
			}
			
			// Call any post response handlers like database commit handlers
			for(CallSpec spec : preResponseCalls) {
				callAndUnwrapException(marshal, spec);
			}
		}
		catch(Exception e) {
			marshal.getContext().setFoundParameter(EXCEPTION_PARAMETER_NAME, Exception.class, e);
			
			
			for(CallSpec spec : normalHanlderFailedCalls) {
				try {
					callAndUnwrapException(marshal, spec);
				}
				catch(Exception ex) {
					log.error("Could not call a failure interceptor.", ex);
				}
			}
			
			int distance = Integer.MAX_VALUE;
			Class exceptionClassForLowestDistance = null;
			for(Class c : exceptionHandlers.keySet()) {
				if(c.isAssignableFrom(e.getClass())) {
					Integer possible = ReflectionUtils.findClassDistance(c, e.getClass());
					if(possible != null && possible < distance) {
						distance = possible;
						exceptionClassForLowestDistance = c;
					}
				}
			}
			
			if(exceptionClassForLowestDistance != null) {
				try {
					result = callAndUnwrapException(marshal, exceptionHandlers.get(exceptionClassForLowestDistance));
					called = exceptionHandlers.get(exceptionClassForLowestDistance);
				}
				catch(Exception ex) {
					// so, we're probably f'd
					log.error("Problem executing an exception handler for exception type: " + exceptionClassForLowestDistance.getName(), ex);
				}
			}
		}
		
		if(called != null) {
			Respondent resp = outputInvestigator.determineTransformers(marshal.getContext(), called.getFocus(), called.getMethod(), result);
			if(resp != null) {
				resp.respond(servletContext, request, response);
			}
		}
		
		for(CallSpec spec : postResponseCalls) {
			try {
				callAndUnwrapException(marshal, spec);
			}
			catch(Exception e) {
				// no better way to handle these, log and go on
				log.error("Problem executing a post response handler", e);
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

	public List<CallSpec> getPreRequestCalls() {
		return preHandleCalls;
	}

	public List<CallSpec> getPreResponseCalls() {
		return preResponseCalls;
	}

	public List<CallSpec> getPostResponseCalls() {
		return postResponseCalls;
	}

	public Location getLocation() {
		return location;
	}

	@Wire
	public void setLocation(Location location) {
		this.location = location;
	}

	public List<CallSpec> getNormalHanlderFailedCalls() {
		return normalHanlderFailedCalls;
	}
	
	
}
