package com.emergentideas.webhandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a method should be called before the method which is annotated.  One of the 
 * great reasons for doing this is so that we can look at all of the loaded objects and compare
 * them to make sure they are compatible for security reasons.  That is, we can make sure an
 * object owned by a user is actually being loaded by that user.
 * @author kolz
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PreCall {
	
	/** Determines what should be called
	 * <pre>
	 * METHOD_OBJECT: The precall method is on the same object as the called method
	 * NAMED_OBJECT: The precall method is on an object whose name will be supplied
	 * TYPED_OBJECT: The precall method is on an object which will be looked up by type
	 * @author kolz
	 *
	 */
	public enum CallFocus { METHOD_OBJECT, NAMED_OBJECT, TYPED_OBJECT }
	
	/**
	 * The method name to call
	 * @return
	 */
	public String value();
	
	public String path() default "";
	
	public CallFocus source() default CallFocus.METHOD_OBJECT;

}
