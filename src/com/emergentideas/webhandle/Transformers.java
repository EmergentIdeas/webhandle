package com.emergentideas.webhandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Transformers {
	public String[] value();
	
	
	// A string which will be turned into a map of properties for the transformer.  This is configurable,
	// but let's try not to get too carried away.  By default this will be a URL encoded string of properties.
	public String properties() default "";
}
