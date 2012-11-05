package com.emergentideas.webhandle.assumptions.oak;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.BasicLoader;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtomBase;
import com.emergentideas.webhandle.bootstrap.FlatFileConfigurationParser;
import com.emergentideas.webhandle.bootstrap.IncludeConfigurationCreator;

import static com.emergentideas.webhandle.Constants.*;

/**
 * Loads a flat file configuration.  It creates all of the objects and integrators necessary
 * for the "oak" set of assumptions.
 * @author kolz
 *
 */
public class AppLoader extends BasicLoader {
	
	
	protected WebAppLocation webApp;
	protected ClassLoader threadClassLoader;
	
	public AppLoader() {
		this(new AppLocation());
	}
	
	public AppLoader(Location location) {
		super(location);
		this.location = location;
		webApp = new WebAppLocation(location);
		webApp.setServiceByName(CLASS_LOADER_NAME, getClass().getClassLoader());
	}
	
	/**
	 * Loads what is assumed to be a flat file configuration
	 * @param configuration
	 */
	public void load(File configuration) throws IOException {
		configuration = configuration.getAbsoluteFile();
		
		webApp.setServiceByName(APPLICATION_ON_DISK_LOCATION, createAppLocation(configuration));
		load(location, new FlatFileConfigurationParser().parse(new FileInputStream(configuration)));
	}
	
	public void load(InputStream configuration, File applicationRoot) throws IOException {
		webApp.setServiceByName(APPLICATION_ON_DISK_LOCATION, applicationRoot.getAbsolutePath());
		load(location, new FlatFileConfigurationParser().parse(configuration));
	}
	
	@Override
	public void load(Location location, List<ConfigurationAtom> configuration) {
		boolean restore = false;
		if(threadClassLoader == null) {
			threadClassLoader = Thread.currentThread().getContextClassLoader();
			restore = true;
		}
		try {
			super.load(location, configuration);
		}
		finally {
			if(restore) {
				Thread.currentThread().setContextClassLoader(threadClassLoader);
			}
		}
	}

	@Override
	protected void loadBootstrapObjects() {
		getIntegrators().add(new ClassFileIntegrator());

		super.loadBootstrapObjects();
		
		// allows us to include referenced configurations
		integrate(this, location, new ConfigurationAtomBase("", "com.emergentideas.webhandle.bootstrap.IncludeConfigurationCreator"), new IncludeConfigurationCreator());
	}

	protected String createAppLocation(File configuration) {
		String confLocation = configuration.getParent();
		if(confLocation.endsWith("/") || confLocation.endsWith("/")) {
			confLocation = confLocation.substring(0, confLocation.length() - 1);
		}
		if(confLocation.endsWith("WEB-INF")) {
			confLocation = confLocation.substring(0, confLocation.length() - 8);
		}
		
		return confLocation;
	}

	@Override
	public void setLocation(Location location) {
		webApp = new WebAppLocation(location);
		super.setLocation(location);
	}

	

}
