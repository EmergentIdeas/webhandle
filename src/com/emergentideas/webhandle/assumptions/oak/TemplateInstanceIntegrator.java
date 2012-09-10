package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

/**
 * Integrates classes marked with a TemplateDef annotation into the library template
 * source (template-library)
 * @author kolz
 *
 */
@Integrate
public class TemplateInstanceIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(location == null || focus == null) {
			return;
		}
		
		TemplateDef def = ReflectionUtils.getAnnotationOnClass(focus.getClass(), TemplateDef.class);
		if(def == null || focus instanceof TemplateInstance == false) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		LibraryTemplateSource lts = (LibraryTemplateSource)webApp.getServiceByName("template-library");
		
		if(lts == null) {
			return;
		}
		
		lts.add(def.value(), (TemplateInstance)focus);
		
	}

}
