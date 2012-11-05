package com.emergentideas.webhandle.assumptions.acorn;

import java.io.File;

import com.emergentideas.webhandle.Constants;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import static com.emergentideas.webhandle.Constants.*;


@Integrate
public class VirtualHostsConfIntegrator implements Integrator {

	VirtualHost virtualHost;
	
	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(atom == null) {
			return;
		}
		
		if("virtual-hosts-conf".equals(atom.getType())) {
			virtualHost.setLoader(loader);
			virtualHost.setLocation(location);
			File hostsFile = new File(atom.getValue());
			if(hostsFile.isAbsolute() == false) {
				File root = new File((String)new WebAppLocation(location).getServiceByName(APPLICATION_ON_DISK_LOCATION));
				hostsFile = new File(root, atom.getValue());
			}
			virtualHost.setConfigurationsLocation(hostsFile);
			virtualHost.reload();
		}

	}

	public VirtualHost getVirtualHost() {
		return virtualHost;
	}

	@Wire
	public void setVirtualHost(VirtualHost virtualHost) {
		this.virtualHost = virtualHost;
	}
	
	

}
