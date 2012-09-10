package com.emergentideas.webhandle.assumptions.oak;

import java.io.File;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Create;
import com.emergentideas.webhandle.bootstrap.Creator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.templates.TripartateFileTemplateSource;

@Create("template-directory")
public class TripartateDirectoryTemplateSourceCreator implements Creator {

	public Object create(Loader loader, Location location,
			ConfigurationAtom atom) {
		WebAppLocation webApp = new WebAppLocation(location);
		String appLocation = (String)webApp.getServiceByName(AppLoader.APPLICATION_ON_DISK_LOCATION);
		if(appLocation == null) {
			return null;
		}
		
		File root = new File(new File(appLocation), atom.getValue());
		return new TripartateFileTemplateSource(root);
	}

}
