package com.emergentideas.webhandle.assumptions.oak;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;


public class ClassFileIntegrator implements Integrator {
	
	protected Logger log = SystemOutLogger.get(ClassFileIntegrator.class);

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(atom == null) {
			return;
		}
		
		if("bin".equals(atom.getType())) {
			WebAppLocation webApp = new WebAppLocation(location);
			String binPath = atom.getValue();
			URL url;
			try {
				if(binPath.contains("://")) {
					url = new URL(binPath);
				}
				else {
					File f = new File((String)webApp.getServiceByName(AppLoader.APPLICATION_ON_DISK_LOCATION));
					f = new File(f, binPath);
					url = f.toURI().toURL();
				}
				
				ClassLoader oldClassLoader = (ClassLoader)webApp.getServiceByName(AppLoader.CLASS_LOADER_NAME);
				ClassLoader classloader = new URLClassLoader(new URL[] {url}, oldClassLoader);
				Thread.currentThread().setContextClassLoader(classloader);
				webApp.setServiceByName(AppLoader.CLASS_LOADER_NAME, classloader);
			}
			catch(Exception e) {
				log.error("Could not add class from " + binPath, e);
			}

		}
		
	}

	
}
