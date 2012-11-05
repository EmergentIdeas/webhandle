package com.emergentideas.webhandle.assumptions.oak;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

import static com.emergentideas.webhandle.Constants.*;

@Integrate
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
			URL[] urls;
			try {
				if(binPath.contains("://")) {
					urls = new URL[] {new URL(binPath)};
				}
				else {
					File f = new File((String)webApp.getServiceByName(APPLICATION_ON_DISK_LOCATION));
					if(binPath.endsWith("/*") || binPath.endsWith("/*.jar")) {
						List<URL> urlsList = new ArrayList<URL>();
						
						// load all the jars in the directory
						File parent = new File(f, binPath.substring(0, binPath.length() - 2));
						for(File child : parent.listFiles()) {
							if(child.getName().toLowerCase().endsWith(".jar")) {
								urlsList.add(child.toURI().toURL());
							}
						}
						urls = urlsList.toArray(new URL[urlsList.size()]);
					}
					else {
						// load a single jar
						f = new File(f, binPath);
						urls = new URL[] {f.toURI().toURL()};
					}
				}
				
				ClassLoader oldClassLoader = (ClassLoader)webApp.getServiceByName(CLASS_LOADER_NAME);
				ClassLoader classloader = new URLClassLoader(urls, oldClassLoader);
				Thread.currentThread().setContextClassLoader(classloader);
				webApp.setServiceByName(CLASS_LOADER_NAME, classloader);
			}
			catch(Exception e) {
				log.error("Could not add class from " + binPath, e);
			}

		}
		
	}

	
}
