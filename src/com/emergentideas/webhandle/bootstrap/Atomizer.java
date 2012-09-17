package com.emergentideas.webhandle.bootstrap;

/**
 * Breaks a configuration like:
 * <pre>
 * 	class->com.emergentideas.webhandle.Class1
 * </pre>
 * into a {@link ConfigurationAtomBase}.  This can be any class extending
 * {@link ConfigurationAtomBase} but it is recommended that it be one of typical
 * sub classes like 
 * @author kolz
 *
 */
public interface Atomizer {
	
	/**
	 * Takes a type and value and turns it into a ConfigurationAtom or one of its subclasses
	 * @param type The configuration type like "class" or "user-files"
	 * @param value The configuration value "com.emergentideas.webhandle.Class1" or "static_content/css". 
	 * @return
	 */
	public ConfigurationAtom atomize(String type, String value);
}
