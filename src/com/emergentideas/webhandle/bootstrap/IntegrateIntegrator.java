package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;

public class IntegrateIntegrator implements Integrator {

	public void integrate(Loader loader, Location location, ConfigurationAtom atom, Object focus) {
		
		if(focus == null) {
			return;
		}
		
		Integrate integrate = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Integrate.class);
		if(integrate != null && focus instanceof Integrator) {
			loader.getIntegrators().add((Integrator)focus);
		}
	}

}
