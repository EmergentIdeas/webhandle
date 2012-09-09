package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.webhandle.Location;

public interface Integrator {

	public void integrate(Loader loader, Location location, ConfigurationAtom atom, Object focus);
}
