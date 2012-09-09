package com.emergentideas.webhandle.bootstrap;

import java.io.IOException;
import java.util.List;

import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.Location;

@Create("class-path-include")
public class IncludeConfigurationCreator implements Creator {

	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		FlatFileConfigurationParser parser = new FlatFileConfigurationParser();
		
		try {
			List<ConfigurationAtom> configuration = parser.parse(StringUtils.getStreamFromClassPathLocation(atom.getValue()));
			loader.load(location, configuration);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

}
