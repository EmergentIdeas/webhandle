package com.emergentideas.webhandle.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;

@Create("config-file-include")
public class IncludeFileConfigurationCreator implements Creator {

	protected Logger log = SystemOutLogger.get(IncludeFileConfigurationCreator.class);
	
	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		FlatFileConfigurationParser parser = new FlatFileConfigurationParser();
		
		try {
			File f = new File(atom.getValue());
			
			if(f.isAbsolute() == false) {
				WebAppLocation webApp = new WebAppLocation(location);
				f = new File(webApp.getAppRootLocationOnDisk(), atom.getValue());
			}
			if(f.exists()) {
				List<ConfigurationAtom> configuration = parser.parse(new FileInputStream(f));
				loader.load(location, configuration);
			}
			else {
				log.error("Could not include configuration file at: " + atom.getValue());
			}
		}
		catch(IOException e) {
			log.error("Could not include configuration file at: " + atom.getValue(), e);
		}
		
		
		return null;
	}

}
