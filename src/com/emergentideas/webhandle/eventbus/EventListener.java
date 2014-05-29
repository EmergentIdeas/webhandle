package com.emergentideas.webhandle.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that is listening for events.
 * @author kolz
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EventListener {
	
	/**
	 * Queues are expected like
	 * <pre>
	 * 		/changed
	 * </pre>
	 * where it is also possible to specify a parameter with an expression like 
	 * <pre>
	 * 		/users/{userId:expression}
	 * </pre>
	 */
	public String[] value();
	
}
