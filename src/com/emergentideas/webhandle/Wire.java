package com.emergentideas.webhandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Wire {

	public enum WireMethod { ANY, NAME, TYPE }
	
	public WireMethod value() default WireMethod.ANY;
}
