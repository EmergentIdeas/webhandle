package com.emergentideas.webhandle.files;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Create;
import com.emergentideas.webhandle.bootstrap.Creator;
import com.emergentideas.webhandle.bootstrap.FocusAndPropertiesAtom;
import com.emergentideas.webhandle.bootstrap.FocusAndPropertiesConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.bootstrap.PropertiesAtom;
import com.emergentideas.webhandle.bootstrap.PropertiesConfigurationAtom;
import com.emergentideas.webhandle.handlers.HandlerInvestigator;

import static com.emergentideas.webhandle.Constants.*;


@Create({"public-resource", "classpath-public-resource", "resource-sink"})
@Integrate
public class PublicResourcesIntegrator implements Creator, Integrator {

	public static final String SHOW_DIRECTORY_CONTENTS = "showDirectoryContents";
	public static final String CACHE_TIME = "cacheTime";
	public static final String DIRECTORY_DEFAULT_FILES = "directoryDefaultFiles";
	
	protected HandlerInvestigator handlerInvestigator;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus != null && focus instanceof StreamableResourceSource) {
			if("public-resource".equals(atom.getType()) || "classpath-public-resource".equals(atom.getType())) {
				StreamableResourcesHandler handler = new StreamableResourcesHandler((StreamableResourceSource)focus);
				if(atom instanceof PropertiesAtom) {
					PropertiesAtom propAtom = (PropertiesAtom)atom;
					handler.setShowDirectoryContents(BooleanUtils.toBoolean(propAtom.getProperties().get(SHOW_DIRECTORY_CONTENTS)));
					String cacheTime = propAtom.getProperties().get(CACHE_TIME);
					if(StringUtils.isNotBlank(cacheTime)) {
						handler.setCacheTime(Integer.parseInt(cacheTime));
					}
					
					String directoryDefaultFiles = propAtom.getProperties().get(DIRECTORY_DEFAULT_FILES);
					if(StringUtils.isNotBlank(directoryDefaultFiles)) {
						handler.setDirectoryDefaultFiles(directoryDefaultFiles);
					}
				}
				handlerInvestigator.analyzeObject(handler);
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
		String resourceLocation;
		
		if(atom instanceof FocusAndPropertiesAtom) {
			resourceLocation = ((FocusAndPropertiesAtom)atom).getFocus();
		}
		else {
			resourceLocation = atom.getValue();
		}
		
		if("public-resource".equals(atom.getType())) {
			File root = new File(new File(appLocation), resourceLocation);
			return new FileStreamableResourceSource(root);
		}
		if("classpath-public-resource".equals(atom.getType())) {
			return new ClasspathFileStreamableResourceSource(resourceLocation);
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
