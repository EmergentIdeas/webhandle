package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.ExceptionHandler;
import com.emergentideas.webhandle.HandlingFail;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.OutputResponseInvestigator;
import com.emergentideas.webhandle.PostResponse;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.PreResponse;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;
import com.emergentideas.webhandle.handlers.ResponseLifecycleHandler;

@Integrate
public class ResponseLifecycleIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(location == null || focus == null) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		Object handler = webApp.getServiceByName("request-handler");
		if(handler == null) {
			return;
		}
		if(handler instanceof ResponseLifecycleHandler == false) {
			return;
		}
		ResponseLifecycleHandler lifecycle = (ResponseLifecycleHandler)handler;
		
		if(focus instanceof HandlerInvestigator) {
			lifecycle.setHandlerInvestigator((HandlerInvestigator)focus);
		}
		if(focus instanceof OutputResponseInvestigator) {
			lifecycle.setOutputInvestigator((OutputResponseInvestigator)focus);
		}
		
		for(CallSpec cs : ReflectionUtils.getMethodsWithAnnotaion(focus, PreRequest.class)) {
			lifecycle.getPreRequestCalls().add(cs);
		}

		for(CallSpec cs : ReflectionUtils.getMethodsWithAnnotaion(focus, PreResponse.class)) {
			lifecycle.getPreResponseCalls().add(cs);
		}
		
		for(CallSpec cs : ReflectionUtils.getMethodsWithAnnotaion(focus, PostResponse.class)) {
			lifecycle.getPostResponseCalls().add(cs);
		}
		
		for(CallSpec cs : ReflectionUtils.getMethodsWithAnnotaion(focus, HandlingFail.class)) {
			lifecycle.getNormalHanlderFailedCalls().add(cs);
		}
		
		List<CallSpec> result = new ArrayList<CallSpec>();
		for(Method m : focus.getClass().getMethods()) {
			ExceptionHandler eh = ReflectionUtils.getAnnotation(m, ExceptionHandler.class);
			if(eh != null) {
				CallSpec cs = new CallSpec(focus, m, false);
				lifecycle.getExceptionHandlers().put(eh.value(), cs);
			}
		}

	}

}
