package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

@Integrate
public class ServiceIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		
		if(focus == null) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		
		Name name = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Name.class);
		if(name != null) {
			webApp.setServiceByName(name.value(), focus);
		}
		
		Type type = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Type.class);
		if(type != null) {
			for(String typeName : type.value()) {
				webApp.setServiceByType(typeName, focus);
			}
		}
	}

}
