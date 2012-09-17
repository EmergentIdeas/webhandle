package com.emergentideas.webhandle.bootstrap;

public interface FocusAndPropertiesAtom extends PropertiesAtom {

	/**
	 * Returns the focus of {@link ConfigurationAtom}.  In the case that it represents
	 * a class with properties like:
	 * <pre>
	 * 		class->com.emergentideas.webhandle.Class1?attr1=one&attr2=two
	 * </pre>
	 * the focus would be <code>com.emergentideas.webhandle.Class1</code>.
	 * @return
	 */
	public String getFocus();
}
