package com.emergentideas.webhandle.files;

import java.io.File;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Create;
import com.emergentideas.webhandle.bootstrap.Creator;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

@Create("public-resource")
@Integrate
public class PublicResourcesIntegrator implements Creator, Integrator {

	protected HandlerInvestigator handlerInvestigator;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null || focus instanceof StreamableResourceSource == false || "public-resource".equals(atom.getType()) == false) {
			return;
		}

		handlerInvestigator.analyzeObject(new StreamableResourcesHandler((StreamableResourceSource)focus));
	}

	public Object create(Loader loader, Location location,
			ConfigurationAtom atom) {
		WebAppLocation webApp = new WebAppLocation(location);
		String appLocation = (String)webApp.getServiceByName(AppLoader.APPLICATION_ON_DISK_LOCATION);
		if(appLocation == null) {
			return null;
		}
		
		File root = new File(new File(appLocation), atom.getValue());
		return new FileStreamableResourceSource(root);
	}

	public HandlerInvestigator getHandlerInvestigator() {
		return handlerInvestigator;
	}

	@Wire
	public void setHandlerInvestigator(HandlerInvestigator handlerInvestigator) {
		this.handlerInvestigator = handlerInvestigator;
	}

	
}
