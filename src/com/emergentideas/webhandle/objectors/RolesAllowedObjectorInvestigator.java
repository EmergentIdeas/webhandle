package com.emergentideas.webhandle.objectors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.CallSpec;
import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.ObjectorInvestigator;
import com.emergentideas.webhandle.Source;

/**
 * Looks for the {@link RolesAllowed} annotation on the class and on the method being called to
 * determine if the set of user groups should be checked.
 * @author kolz
 *
 */
public class RolesAllowedObjectorInvestigator implements
		ObjectorInvestigator {

	public CallSpec determineObjector(Object focus, Method method) {
		
		RolesAllowed rg = ReflectionUtils.getAnnotationOnClass(getClassToExamine(focus, method), RolesAllowed.class);
		
		if(rg == null) {
			rg = ReflectionUtils.getAnnotation(method, RolesAllowed.class);
		}
		
		if(rg != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("rolesAllowed", rg.value());
			CallSpec spec = createObjectMethodSpec(focus, method, rg);
			spec.setCallSpecificProperties(properties);
			
			return spec;
		}
		
		
		return null;
	}
	
	protected Class getClassToExamine(Object focus, Method method) {
		if(focus != null) {
			return focus.getClass();
		}
		return method.getDeclaringClass();
	}
	
	protected CallSpec createObjectMethodSpec(Object focus, Method method, RolesAllowed rg) {
		return new CallSpec(this, ReflectionUtils.getFirstMethod(this.getClass(), "hasGroup"), false);
	}
	
	/**
	 * Returns true if the user has one of the allowed roles.  Otherwise throws a security exception.
	 * @param userRoles The roles as user has
	 * @param rolesAllowed The set of roles that a user must have one of.
	 */
	public boolean hasGroup(@Source(Constants.USER_INFORMATION_SOURCE_NAME) List<String> userRoles, @Source(Constants.ANNOTATION_PROPERTIES_SOURCE_NAME)List<String> rolesAllowed) {
		if(rolesAllowed == null || rolesAllowed.size() == 0) {
			return true;
		}
		
		if(userRoles == null) {
			throw new SecurityException("The user has no roles (may not be signed in) and so is not authorized to perform this function.");
		}
		for(String allowed : rolesAllowed) {
			if(userRoles.contains(allowed)) {
				return true;
			}
		}
		
		throw new SecurityException("The user does not have the needed roles and so is not authorized to perform this function.");
	}

}