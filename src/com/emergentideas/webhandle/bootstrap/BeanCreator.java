package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Location;

@Create({"", "class"})
public class BeanCreator implements Creator {
	
	protected Logger log = SystemOutLogger.get(BeanCreator.class);

	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(atom.getValue()).newInstance();
		}
		catch(Throwable t) {
			log.error("Could not load object with class: " + atom.getValue(), t);
		}
		return null;
	}

}
