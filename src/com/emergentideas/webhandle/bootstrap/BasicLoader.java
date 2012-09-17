package com.emergentideas.webhandle.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;

/**
 * Loads and integrates configuration atoms into a location
 * @author kolz
 *
 */
public class BasicLoader implements Loader {
	
	/**
	 * a special type which indicates that we should use a declared loader class to load the configuration atoms
	 * instead of this
	 * 
	 */
	public static final String DELEGATE_LOADER_TYPE = "lets-get-this-party-started";
	
	/**
	 * A name for the {@link Location} which contains all of the information at the application level.  This can
	 * be used like:
	 * <pre>
	 * 		location.get("app-level-location")
	 * </pre>
	 */
	public static final String APP_LEVEL_LOCATION = "app-level-location";

	// objects which will create objects based on a configuration atom
	protected Map<String, Creator> creators = new HashMap<String, Creator>();
	
	// objects which integrate created objects and configurations into the existing infrastructure
	protected List<Integrator> integrators = new ArrayList<Integrator>();
	
	// objects which parse individual configuration lines into configuration atoms
	protected List<AtomizerConfiguration> atomizerConfigurations = new ArrayList<AtomizerConfiguration>();
	
	// the location which holds information for the application
	protected Location location = new AppLocation();
	
	protected Logger log = SystemOutLogger.get(BasicLoader.class);
	
	public BasicLoader() {
		location.put(APP_LEVEL_LOCATION, location);
		
		WebAppLocation webApp = new WebAppLocation(location);
		webApp.setServiceByType(Location.class.getName(), location);
		
		loadBootstrapObjects();
	}
	
	protected void loadBootstrapObjects() {
		getIntegrators().add(new CreateIntegrator());
		getIntegrators().add(new IntegrateIntegrator());

		
		BeanCreator creator = new BeanCreator();
		integrate(this, location, null, creator);
		
	}
	
	protected void integrate(Loader loader, Location location, ConfigurationAtom conf, Object focus) {
		for(Integrator integrator : integrators.toArray(new Integrator[integrators.size()])) {
			// using an array here because an integrator will sometimes add an integrator to the loader.
			integrator.integrate(loader, location, conf, focus);
		}
	}
	
	public void load(Location location, List<ConfigurationAtom> configuration) {
		
		Loader delegatedLoader = findDelegateLoader(configuration);
		
		if(delegatedLoader == null) {
			// If they wanted something else to loade the configuration we'll use it, otherwise we'll use
			// the current loader.
			delegatedLoader = this;
		}
		
		for(int i = 0; i < configuration.size(); i++) {
			ConfigurationAtom atom = configuration.get(i);
			try {
				
				AtomizerConfiguration atomizerConfig = findAtomizer(atom);
				if(atomizerConfig != null) {
					// If we've got an atomizer for this type, create a new configuration
					// atom and put it in the list.
					atom = atomizerConfig.getAtomizer().atomize(atom.getType(), atom.getValue());
					configuration.set(i, atom);
				}
				
				Object created = create(delegatedLoader, location, atom);
				integrate(delegatedLoader, location, atom, created);
			}
			catch(Exception e) {
				log.error("Could not process configuration atom with type->value: " + atom.getType() + "->" + atom.getValue(), e);
			}
		}
	}
	
	protected AtomizerConfiguration findAtomizer(ConfigurationAtom atom) {
		for(AtomizerConfiguration ac : atomizerConfigurations) {
			if(ac.getTypePattern().matcher(atom.getType()).matches()) {
				return ac;
			}
		}
		
		return null;
	}
	
	/**
	 * If an atom with the type <code>lets-get-this-party-started</code> is found, it will load the value
	 * as a class and remove the atom from the configuration list.
	 * @param configuration
	 * @return
	 */
	protected Loader findDelegateLoader(List<ConfigurationAtom> configuration) {
		Iterator<ConfigurationAtom> atoms = configuration.iterator();
		while(atoms.hasNext()) {
			ConfigurationAtom atom = atoms.next();
			if(DELEGATE_LOADER_TYPE.equals(atom.getType())) {
				atoms.remove();
				ConfigurationAtom objAtom = new ConfigurationAtomBase("class", atom.getValue());
				Object created = create(this, location, atom);
				if(created != null && created instanceof Loader) {
					return (Loader)created;
				}
			}
		}
		
		return null;
	}
	
	protected Object create(Loader loader, Location location, ConfigurationAtom atom) {
		for(String type : creators.keySet()) {
			if(type.equals(atom.getType())) {
				return creators.get(type).create(loader, location, atom);
			}
		}
		
		return null;
	}

	public Map<String, Creator> getCreators() {
		return creators;
	}

	public List<Integrator> getIntegrators() {
		return integrators;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<AtomizerConfiguration> getAtomizerConfigurations() {
		return atomizerConfigurations;
	}
	
}
