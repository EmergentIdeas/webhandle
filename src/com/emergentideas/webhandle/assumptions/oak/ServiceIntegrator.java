package com.emergentideas.webhandle.assumptions.oak;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

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
		
		Resource r = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Resource.class);
		if(r != null) {
			String serviceName = null;
			if(StringUtils.isBlank(r.name())) {
				serviceName = focus.getClass().getSimpleName();
				if(serviceName.length() > 0) {
					serviceName = Character.toLowerCase(serviceName.charAt(0)) + serviceName.substring(1);
				}
			}
			else {
				serviceName = r.name();
			}
			
			if(StringUtils.isBlank(serviceName) == false) {
				webApp.setServiceByName(serviceName, focus);
			}
			
			if(Object.class.equals(r.type()) == false) {
				webApp.setServiceByType(r.type().getName(), focus);
			}
		}
	}

}
