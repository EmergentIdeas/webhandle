package com.emergentideas.webhandle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.utils.ReflectionUtils;

/**
 * Defines for a given method whether a value may be loaded by name, by type, or by both.  Used
 * by the autowire integrator to grant more control to the bean being wired about how it should
 * be wired.
 * @author kolz
 *
 */
public class AutoWireSourceSetInvestigator implements SourceSetInvestigator {
	
	public static final String NAMED = "named";
	public static final String TYPED = "typed";
	public static final String NO_SOURCE = "no source is allowed";

	public <T> String[] determineAllowedSourceSets(Object focus, Method method,
			Class<T> parameterClass, String parameterName,
			Annotation[] parameterAnnotations, InvocationContext context) {
		// The rule is, if there's a wire annotation on a method, we use it
		// If there's none on the method, we'll look for one on the class
		// If neither exist, then we'll say there's no source which can wire this
		// method
		Wire w = ReflectionUtils.getAnnotation(method, Wire.class);
		if(w == null) {
			w = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Wire.class);
		}
		
		if(w == null) {
			return new String[] { NO_SOURCE };
		}

		if(w.value() == Wire.WireMethod.ANY) {
			return new String[] { NAMED, TYPED };
		}
		if(w.value() == Wire.WireMethod.NAME) {
			return new String[] { NAMED };
		}
		if(w.value() == Wire.WireMethod.TYPE) {
			return new String[] { TYPED };
		}
		
		return new String[] { NO_SOURCE };
	}

}
