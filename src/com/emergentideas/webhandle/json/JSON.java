package com.emergentideas.webhandle.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be placed on Handler methods that would like their returned objects
 * to be serialized as JSON.
 * @author kolz
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JSON {
	
	/** 
	 * The json serialization profiles to use, in the order that they
	 * should be used.
	 * 
	 */
	public String[] value() default "default";

}
