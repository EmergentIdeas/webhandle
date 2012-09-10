package com.emergentideas.webhandle.bootstrap;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;

@Integrate
public class CreateIntegrator implements Integrator {

	public void integrate(Loader loader, Location location, ConfigurationAtom atom, Object focus) {
		
		if(focus == null) {
			return;
		}
		
		Create create = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Create.class);
		if(create != null && focus instanceof Creator) {
			for(String type : create.value()) {
				loader.getCreators().put(type, (Creator)focus);
			}
		}
	}

}
