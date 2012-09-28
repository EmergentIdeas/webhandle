package com.emergentideas.webhandle.bootstrap;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.emergentideas.webhandle.sources.PropertiesValueSource;

/**
 * Wires a bean with the values from a properties file on the classpath.  Entries will be in
 * the form of:
 * <pre>
 * classpath-property-wire->the/properties/file/path.properties?objectName=theobjectname
 * </pre>
 * or
 * <pre>
 * classpath-property-wire->the/properties/file/path.properties?objectType=theobjecttype
 * </pre>
 * @author kolz
 *
 */
@Create("classpath-property-wire")
@Integrate
@Atomize("classpath-property-wire")
public class WireProperties extends URLFocusAndPropertiesAtomizer implements Creator, Integrator, Atomizer {

	protected Logger log = SystemOutLogger.get(WireProperties.class);
	protected ParameterMarshalConfiguration conf;
	
	public WireProperties() {
		conf = new IntegratorConfiguration();
	}

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null || atom == null || focus instanceof Properties == false) {
			return;
		}

		if("classpath-property-wire".equals(atom.getType())) {
			FocusAndPropertiesAtom focusAtom = (FocusAndPropertiesAtom)atom;
			WebAppLocation webApp = new WebAppLocation(location);
			
			Properties data = (Properties)focus;
			
			Object toWire = null;
			String objectName = focusAtom.getProperties().get("objectName");
			String objectType = focusAtom.getProperties().get("objectType");
			if(org.apache.commons.lang.StringUtils.isBlank(objectName) == false) {
				toWire = webApp.getServiceByName(objectName);
			}
			else if(org.apache.commons.lang.StringUtils.isBlank(objectType) == false) {
				toWire = webApp.getServiceByType(objectType);
			}
			
			if(toWire != null) {
				InvocationContext context = new InvocationContext();
				context.setFoundParameter(Location.class, location);
				
				ParameterMarshal marshal = new ParameterMarshal(conf, context);
				marshal.addSource("properties", new PropertiesValueSource(data));
				
				for(String key : (Set<String>)(Set)data.keySet()) {
					Method m = ReflectionUtils.getSetterMethod(toWire, key);
					if(m != null) {
						try {
							marshal.call(toWire, m, true);
						}
						catch(Exception e) {
							log.error("Couldn't wire method: " + m.getName(), e);
							// if we can't wire, I think that's okay
						}
					}
				}
			}
		}

	}

	public Object create(Loader loader, Location location,
			ConfigurationAtom atom) {
		FocusAndPropertiesAtom focusAtom = (FocusAndPropertiesAtom)atom;
		Properties properties = new Properties();
		try {
			properties.load(StringUtils.getStreamFromClassPathLocation(focusAtom.getFocus()));
		}
		catch(Exception e) {
			log.error("Could not load properties file from classpath: " + focusAtom.getFocus(), e);
		}
		return properties;
	}

}
