package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.templates.TemplateSource;

/**
 * Integrates new template sources into the composite template source.
 * @author kolz
 *
 */
@Integrate
public class CompositeTemplateSourcesIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(location == null || focus == null) {
			return;
		}

		WebAppLocation webApp = new WebAppLocation(location);
		TemplateSource ts = webApp.getTemplateSource();
		if(ts != null && ts instanceof CompositeTemplateSource && focus instanceof TemplateSource) {
			((CompositeTemplateSource)ts).addTemplateSource((TemplateSource)focus);
		}
	}

}
