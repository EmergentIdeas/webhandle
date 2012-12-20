package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

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
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;

@Integrate
public class AutoWireIntegrator implements Integrator {
	
	protected ParameterMarshalConfiguration conf;
	protected Logger logger = SystemOutLogger.get(this.getClass());
	
	public AutoWireIntegrator() {
		
		conf = new IntegratorConfiguration();
		conf.getSourceSetInvestigators().add(new AutoWireSourceSetInvestigator());
	}

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		
		InvocationContext context = new InvocationContext();
		context.setFoundParameter(Location.class, location);
		
		ParameterMarshal marshal = new ParameterMarshal(conf, context);
		marshal.addSource("named", new NamedServicesValueSource());
		marshal.addSource("typed", new TypedServicesValueSource());
		
		Class c = focus.getClass();
		while(c.equals(Object.class) == false) {
			for(Field f : c.getDeclaredFields()) {
				Resource r = ReflectionUtils.getAnnotation(f, Resource.class);
				if(r != null) {
					String name = f.getName();
					String type = f.getType().getName();
					if(StringUtils.isBlank(r.name()) == false) {
						name = r.name();
					}
					if(r.type().equals(Object.class) == false) {
						type = r.type().getName();
					}
					
					Object found = null;
					Object service = webApp.getServiceByName(name);
					if(service != null) {
						if(f.getType().isAssignableFrom(service.getClass())) {
							found = service;
						}
					}
					if(found == null) {
						service = webApp.getServiceByType(type);
						if(service != null) {
							if(f.getType().isAssignableFrom(service.getClass())) {
								found = service;
							}
						}
					}
					
					if(found != null) {
						f.setAccessible(true);
						try {
							f.set(focus, found);
						}
						catch(Exception e) {
							logger.error("Could not wire field " + f.getName() + " on object of class " + focus.getClass().getName(), e);
						}

					}
				}
			}
			
			c = c.getSuperclass();
		}
		
		for(Method m : focus.getClass().getMethods()) {
			NoInject ni = ReflectionUtils.getAnnotation(m, NoInject.class);
			if(ni != null) {
				continue;
			}
			
			Wire w = ReflectionUtils.getAnnotation(m, Wire.class);
			boolean classBasedAnnotation = false;
			if(w == null) {
				w = ReflectionUtils.getAnnotationOnClass(focus.getClass(), Wire.class);
				classBasedAnnotation = true;
			}
			
			Resource r = ReflectionUtils.getAnnotation(m, Resource.class);
			
			if(w != null || r != null) {
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

}
