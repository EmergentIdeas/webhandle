package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.webhandle.Location;

@Create({"", "class"})
public class BeanCreator implements Creator {

	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(atom.getValue()).newInstance();
		}
		catch(Throwable t) {
			
		}
		return null;
	}

}
