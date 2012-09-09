package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.webhandle.Location;

public interface Creator {

	public Object create(Loader loader, Location location, ConfigurationAtom atom);
	
}
