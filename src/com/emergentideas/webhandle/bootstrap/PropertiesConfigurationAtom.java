package com.emergentideas.webhandle.bootstrap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertiesConfigurationAtom extends ConfigurationAtomBase implements PropertiesAtom {
	
	protected Map<String, String> properties = Collections.synchronizedMap(new HashMap<String, String>());
	
	public Map<String, String> getProperties() {
		return properties;
	}

}
