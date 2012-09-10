package com.emergentideas.webhandle.templates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a template so that any integrator will know that it's a template and 
 * will know which name to use when entering it in a library
 * @author kolz
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TemplateDef {
	public String value();
}
