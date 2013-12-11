package com.emergentideas.webhandle.db;

import javax.annotation.Resource;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

@Integrate
public class ObjectChangeListenerIntegrator implements Integrator {
	
	@Resource
	protected ObjectEventInterchange objectEventInterchange;

	@Override
	public void integrate(Loader loader, Location location, ConfigurationAtom atom, Object focus) {
		if(objectEventInterchange != null) {
			if(focus != null && focus instanceof ObjectChangeListener) {
				objectEventInterchange.addListener((ObjectChangeListener)focus);
			}
		}
	}

}
