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
import com.emergentideas.webhandle.eventbus.EventBus;

@Integrate
public class EventBusIntegrator implements Integrator {

	@Resource
	protected EventBus applicationEventBus;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		
		if(focus == null) {
			return;
		}
		
		if(applicationEventBus != null) {
			applicationEventBus.register(focus);
		}
	}

}
