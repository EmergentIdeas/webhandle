package com.emergentideas.webhandle.bootstrap;

import java.util.List;
import java.util.Map;

import com.emergentideas.webhandle.Location;

/**
 * Loads and integrates objects in response to a {@link ConfigurationAtomBase} list.
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
	 * {@link ConfigurationAtomBase} objects.  The map here has the type as the key. 
	 * @return
	 */
	public Map<String, Creator> getCreators();
	
	/**
	 * Objects called to add the newly created object into the infrastructure.
	 * @return
	 */
	public List<Integrator> getIntegrators();
	
	/**
	 * Configurations that will transform an existing {@link ConfigurationAtom} into
	 * an atom with more details for the creator and integrators.
	 * @return
	 */
	public List<AtomizerConfiguration> getAtomizerConfigurations();
}
