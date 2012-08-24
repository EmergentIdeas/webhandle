package com.emergentideas.webhandle.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Handle {
	
	/**
	 * Paths are expected like
	 * <pre>
	 * 		/users/{userId}/details
	 * </pre>
	 * where it is also possible to specify a parameter with an expression like 
	 * <pre>
	 * 		/users/{userId:expression}/details
	 * </pre>
	 */
	public String[] path();
	
	public HttpMethod[] method() default HttpMethod.ANY;

}
