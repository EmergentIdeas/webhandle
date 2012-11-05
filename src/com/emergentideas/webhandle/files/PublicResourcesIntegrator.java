package com.emergentideas.webhandle.files;

import java.io.File;
import java.util.Map;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Create;
import com.emergentideas.webhandle.bootstrap.Creator;
import com.emergentideas.webhandle.bootstrap.FocusAndPropertiesConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.bootstrap.PropertiesConfigurationAtom;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

import static com.emergentideas.webhandle.Constants.*;


@Create({"public-resource", "classpath-public-resource", "resource-sink"})
@Integrate
public class PublicResourcesIntegrator implements Creator, Integrator {

	protected HandlerInvestigator handlerInvestigator;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus != null && focus instanceof StreamableResourceSource) {
			if("public-resource".equals(atom.getType()) || "classpath-public-resource".equals(atom.getType())) {
				handlerInvestigator.analyzeObject(new StreamableResourcesHandler((StreamableResourceSource)focus));
			}
		}

	}

	public Object create(Loader loader, Location location,
			ConfigurationAtom atom) {
		WebAppLocation webApp = new WebAppLocation(location);
		String appLocation = (String)webApp.getServiceByName(APPLICATION_ON_DISK_LOCATION);
		if(appLocation == null) {
			return null;
		}
		
		if("public-resource".equals(atom.getType())) {
			File root = new File(new File(appLocation), atom.getValue());
			return new FileStreamableResourceSource(root);
		}
		if("classpath-public-resource".equals(atom.getType())) {
			return new ClasspathFileStreamableResourceSource(atom.getValue());
		}
		if("resource-sink".equals(atom.getType())) {
			File root = new File(new File(appLocation), ((FocusAndPropertiesConfigurationAtom)atom).getFocus());
			
			Object o = new FileStreamableResourceSink(root);
			Map<String, String> props = ((PropertiesConfigurationAtom)atom).getProperties();
			if(props.containsKey("name")) {
				webApp.setServiceByName(props.get("name"), o);
			}
			
			return o;
		}
		
		return null;
	}

	public HandlerInvestigator getHandlerInvestigator() {
		return handlerInvestigator;
	}

	@Wire
	public void setHandlerInvestigator(HandlerInvestigator handlerInvestigator) {
		this.handlerInvestigator = handlerInvestigator;
	}

	
}
