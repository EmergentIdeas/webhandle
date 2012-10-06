package com.emergentideas.webhandle.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.Atomize;
import com.emergentideas.webhandle.bootstrap.Atomizer;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.FocusAndPropertiesAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.bootstrap.URLFocusAndPropertiesAtomizer;

@Integrate
public class DbInitIntegrator implements Integrator {

	protected static int persistenceConfigNumber = 0;
	protected static String persistenceConfigPrefix = "automaticPersistence";
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(atom == null || atom.getType().equals("db-init") == false) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);

        ProxiedThreadLocalEntityManager em = webApp.getServiceByType(ProxiedThreadLocalEntityManager.class);
        if(em != null) {
        	String confName = atom.getValue();
        	if(StringUtils.isBlank(confName) || "*automatic".equals(confName)) {
        		confName = generateConfigurationName(webApp);
        	}
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(confName);
        	em.setFactory(factory);
        }

	}
	
	protected String generateConfigurationName(WebAppLocation webApp) {
		String configName = persistenceConfigPrefix + (persistenceConfigNumber++) + System.currentTimeMillis();
		webApp.setServiceByName(Constants.NAME_OF_PERSISTENCE_UNIT, configName);
		return configName;
	}

}
