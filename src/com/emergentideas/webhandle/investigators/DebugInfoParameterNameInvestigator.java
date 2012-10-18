package com.emergentideas.webhandle.investigators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ParameterNameInvestigator;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;

public class DebugInfoParameterNameInvestigator implements
		ParameterNameInvestigator {
	protected Paranamer paranamer = new CachingParanamer(new BytecodeReadingParanamer());

	public <T> String determineParameterName(Object focus, Method method,
			Class<T> parameterClass, Annotation[] parameterAnnotations,
			InvocationContext context, Integer argumentIndex) {
		try {
			if(argumentIndex != null && argumentIndex >= 0) {
				String[] parameterNames = paranamer.lookupParameterNames(method);
				if(parameterNames != null && parameterNames.length > argumentIndex) {
					return parameterNames[argumentIndex];
				}
			}
		}
		catch(ParameterNamesNotFoundException e) {
			e.printStackTrace();
			// no debug info :(
		}
		
		return null;
	}

}
