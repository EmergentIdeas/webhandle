package com.emergentideas.webhandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation saying that any booleans with a setter should be cleared. This
 * is used primarily for objects with values injected from the request where some
 * of the members map to check boxes. Since an empty/unselected check box doesn't
 * send any parameter, something special has to be done to set mapped values to
 * false. This annotation allows you to specify that all boolean members or only
 * certain boolean members should be cleared.
 * @author kolz
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClearBooleans {
	
	/**
	 * Specifies the members which should be cleared. If blank than all boolean
	 * members should be cleared. 
	 */
	public String[] value() default {};
	
	/**
	 * Specifies which members should not be cleared. Useful in the case where all
	 * but one or two boolean members should be injected from the request. Alternatively,
	 * the setter could be marked with a {@link NoInject} annotation.
	 * @return
	 */
	public String[] dontClear() default {};
	
	/**
	 * Defines to what value the boolean should be cleared. I can't think of any case
	 * where you'd clear everything to true, but what the hell. 
	 * @return
	 */
	public boolean clearValue() default false;
}
