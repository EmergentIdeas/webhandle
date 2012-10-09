package com.emergentideas.webhandle.objectors;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ObjectorInvestigator;
import com.emergentideas.webhandle.PreCall;
import com.emergentideas.webhandle.PreCall.CallFocus;
import com.emergentideas.webhandle.WebAppLocation;

/**
 * Looks for the {@link PreCall} annotation and creates an objector call if it is found
 * @author kolz
 *
 */
public class PreCallObjectorInvestegator implements ObjectorInvestigator {

	public CallSpec determineObjector(Object focus, Method method, InvocationContext context) {
		PreCall pc = ReflectionUtils.getAnnotation(method, PreCall.class); 
		
		if(pc == null) {
			pc = ReflectionUtils.getAnnotationOnClass(getClassToExamine(focus, method), PreCall.class);
		}
		
		if(pc == null) {
			return null;
		}
		
		WebAppLocation webApp = new WebAppLocation(context.getLocation());
		
		Object precallObject = null;
		if(pc.source() == CallFocus.METHOD_OBJECT) {
			precallObject = focus;
		}
		else if(StringUtils.isBlank(pc.path()) == false) {
			if(pc.source() == CallFocus.NAMED_OBJECT) {
				precallObject = webApp.getServiceByName(pc.path()); 
			}
			else if(pc.source() == CallFocus.TYPED_OBJECT) {
				precallObject = webApp.getServiceByType(pc.path()); 
			}
		}
		
		if(precallObject == null) {
			return null;
		}
		
		CallSpec cs = ReflectionUtils.getFirstMethodCallSpec(precallObject, pc.value());
		if(cs != null) {
			cs.setFailOnMissingParameter(false);
		}
		
		return cs;
	}

	protected Class getClassToExamine(Object focus, Method method) {
		if(focus != null) {
			return focus.getClass();
		}
		return method.getDeclaringClass();
	}

}
