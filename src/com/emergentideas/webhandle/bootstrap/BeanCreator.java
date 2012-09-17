package com.emergentideas.webhandle.bootstrap;

import java.lang.reflect.Method;
import java.util.Map;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;

@Create({"", "class"})
public class BeanCreator implements Creator {
	
	protected ParameterMarshalConfiguration conf;
	protected Logger log = SystemOutLogger.get(BeanCreator.class);
	
	public BeanCreator() {
		conf = new WebParameterMarsahalConfiguration();
	}

	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		
		String className;
		
		if(atom instanceof FocusAndPropertiesAtom) {
			className = ((FocusAndPropertiesAtom)atom).getFocus();
		}
		else {
			className = atom.getValue();
		}
		
		try {
			Object o = Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
			
			if(atom instanceof FocusAndPropertiesAtom) {
				assignProperties(o, ((FocusAndPropertiesAtom)atom).getProperties());
			}
			
			return o;
		}
		catch(Throwable t) {
			log.error("Could not load object with class: " + atom.getValue(), t);
		}
		return null;
	}
	
	/**
	 * Assign the properties from the configuration.
	 * @param focus
	 * @param properties
	 */
	protected void assignProperties(Object focus, Map<String, String> properties) {
		ParameterMarshal marshal = new ParameterMarshal(conf);
		
		for(String propertyName : properties.keySet()) {
			Method m = ReflectionUtils.getSetterMethod(focus, propertyName);
			try {
				marshal.call(focus, m, true, "creation-properties", properties);
			}
			catch(Exception e) {
				log.error("Could not set configuration property: " + propertyName, e);
			}
		}
	}

}
