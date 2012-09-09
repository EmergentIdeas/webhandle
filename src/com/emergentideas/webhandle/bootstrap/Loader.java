package com.emergentideas.webhandle.bootstrap;

import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.Location;

/**
 * Loads and integrates objects in response to a {@link ConfigurationAtom} list.
 * @author kolz
 *
 */
public interface Loader {

	/**
	 * Loads a bunch of configuration atoms.
	 * @param location The location into which to load the configuration data
	 * @param configuration
	 */
	public void load(Location location, List<ConfigurationAtom> configuration);
	
	/**
	 * Gets the set of beans for this loader which will create objects in response to 
	 * {@link ConfigurationAtom} objects.  The map here has the type as the key. 
	 * @return
	 */
	public Map<String, Creator> getCreators();
	
	/**
	 * Objects called to add the newly created object into the infrastructure.
	 * @return
	 */
	public List<Integrator> getIntegrators();
}
