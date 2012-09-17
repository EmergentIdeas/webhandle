package com.emergentideas.webhandle.bootstrap;

import java.util.Map;

public interface PropertiesAtom extends ConfigurationAtom {

	/**
	 * Gets the properties associated with a configuration atom.  In the case of classes,
	 * these may be configuration parameters.
	 * @return
	 */
	public Map<String, String> getProperties();
}
