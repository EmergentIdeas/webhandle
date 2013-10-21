package com.emergentideas.webhandle.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation should be placed on classes that are able to take objects and serialize
 * them to a JSON format.
 * @author kolz
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JSONSerializer {
	
	/**
	 * The serialization profiles that this serializer will implement.
	 * @return
	 */
	public String[] value() default "default";

}
