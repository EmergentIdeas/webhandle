package com.emergentideas.webhandle.assumptions.oak;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Init;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NamedServicesValueSource;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.TypedServicesValueSource;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;

@Integrate
public class InitIntegrator implements Integrator {
	
	protected ParameterMarshalConfiguration conf;
	protected Logger logger = SystemOutLogger.get(this.getClass());
	protected EntityManager entityManager;
	
	public InitIntegrator() {
		conf = new IntegratorConfiguration();
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
		marshal.addSource("typed", new TypedServicesValueSource());
		
		for(Method m : focus.getClass().getMethods()) {
			Init w = ReflectionUtils.getAnnotation(m, Init.class);
			
			if(w == null) {
				continue;
			}
			
			try {
				if(entityManager != null) {
					entityManager.getTransaction().begin();
				}
				marshal.call(focus, m);
				if(entityManager != null) {
					entityManager.getTransaction().commit();
				}
			}
			catch(Exception e) {
				if(entityManager != null) {
					entityManager.getTransaction().rollback();
				}
				logger.error("Could not init method " + m.getName() + " on object of class " + focus.getClass().getName(), e);
			}
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Wire
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	

}
