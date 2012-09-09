package com.emergentideas.webhandle.configurations;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NamedTransformer;
import com.emergentideas.webhandle.ObjectorInvestigator;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.ParameterNameInvestigator;
import com.emergentideas.webhandle.ParameterTransformersInvestigator;
import com.emergentideas.webhandle.SourceSetInvestigator;
import com.emergentideas.webhandle.ValueTransformer;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

/**
 * Watches for new object creations and integrates the new objects into the parameter marshal web configuration
 * if the new objects will fit there.
 * @author kolz
 *
 */
@Integrate
public class ParameterMarshalIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		Object o = webApp.getServiceByName(WebAppLocation.WEB_PARAMETER_MARSHAL_CONFIGURATION);
		if(o == null || o instanceof ParameterMarshalConfiguration == false) {
			return;
		}
		
		ParameterMarshalConfiguration conf = (ParameterMarshalConfiguration)o;
		
		
		// Load all the investigators
		if(focus instanceof ParameterNameInvestigator) {
			conf.getParameterNameInvestigators().add((ParameterNameInvestigator)focus);
		}
		if(focus instanceof ObjectorInvestigator) {
			conf.getObjectorInvestigators().add((ObjectorInvestigator)focus);
		}
		if(focus instanceof SourceSetInvestigator) {
			conf.getSourceSetInvestigators().add((SourceSetInvestigator)focus);
		}
		if(focus instanceof ParameterTransformersInvestigator) {
			conf.getTransformersInvestigators().add((ParameterTransformersInvestigator)focus);
		}
		
		
		// Load transformers
		if(focus instanceof ValueTransformer<?, ?, ?>) {
			NamedTransformer nt = ReflectionUtils.getAnnotationOnClass(focus.getClass(), NamedTransformer.class);
			if(nt != null) {
				conf.getTransformers().put(nt.value(), (ValueTransformer<?, ?, ?>)focus);
			}
			else {
				// We'll assume that this is a type transformer
				conf.getTypeTransformers().add((ValueTransformer<?, ?, ?>)focus);
			}
		}
		
	}

}
