package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.Method;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.AutoWireSourceSetInvestigator;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NamedServicesValueSource;
import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.TypedServicesValueSource;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;

@Integrate
public class AutoWireIntegrator implements Integrator {
	
	protected ParameterMarshalConfiguration conf;
	protected Logger logger = SystemOutLogger.get(this.getClass());
	
	public AutoWireIntegrator() {
		
		conf = new WebParameterMarsahalConfiguration();
		conf.getSourceSetInvestigators().add(new AutoWireSourceSetInvestigator());
	}

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null) {
			return;
		}
		
		InvocationContext context = new InvocationContext();
		context.setFoundParameter(Location.class, location);
		
		ParameterMarshal marshal = new ParameterMarshal(conf, context);
		marshal.addSource("named", new NamedServicesValueSource());
		marshal.addSource("type", new TypedServicesValueSource());
		
		for(Method m : focus.getClass().getMethods()) {
			Wire w = ReflectionUtils.getAnnotation(m, Wire.class);
			
			boolean classBasedAnnotation = false;
			if(w == null) {
				w = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Wire.class);
				classBasedAnnotation = true;
			}
			
			if(w == null) {
				continue;
			}
			
			NoInject ni = ReflectionUtils.getAnnotation(m, NoInject.class);
			if(ni != null) {
				continue;
			}
			
			if(classBasedAnnotation && ReflectionUtils.isSetterMethod(m) == false) {
				// If it's a class based annotation, we only want to call setter methods
				continue;
			}
			
			try {
				marshal.call(focus, m);
			}
			catch(Exception e) {
				logger.error("Could not wire method " + m.getName() + " on object of class " + focus.getClass().getName(), e);
			}
		}
	}

}
