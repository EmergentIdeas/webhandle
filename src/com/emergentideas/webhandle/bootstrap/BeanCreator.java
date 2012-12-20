package com.emergentideas.webhandle.bootstrap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.vfs.Vfs.File;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.ParameterMarshalConfiguration;
import com.emergentideas.webhandle.configurations.IntegratorConfiguration;
import com.emergentideas.webhandle.configurations.WebParameterMarsahalConfiguration;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

@Create({"", "class"})
public class BeanCreator implements Creator {
	
	protected ParameterMarshalConfiguration conf;
	protected Logger log = SystemOutLogger.get(BeanCreator.class);
	
	public BeanCreator() {
		conf = new IntegratorConfiguration();
	}

	public Object create(Loader loader, Location location, ConfigurationAtom atom) {
		
		String className;
		
		if(atom instanceof FocusAndPropertiesAtom) {
			className = ((FocusAndPropertiesAtom)atom).getFocus();
		}
		else {
			className = atom.getValue();
		}
		
		if(className.contains("*") || className.contains("?") || className.contains("+")) {
			final Pattern pat = Pattern.compile("(" + className + ")\\.class");
			final List<String> foundClassNames = new ArrayList<String>();
			
			Reflections reflections = new Reflections("", new Scanner() {

				public void setConfiguration(Configuration configuration) {}

				public Multimap<String, String> getStore() {
					return null;
				}

				public void setStore(Multimap<String, String> store) {	}

				public Scanner filterResultsBy(Predicate<String> filter) {
					return null;
				}

				public boolean acceptsInput(String file) {
					Matcher m = pat.matcher(file);
					if(m.matches()) {
						
						String cn = m.group(1);
						if(cn.contains("$") == false) {
							foundClassNames.add(cn);
						}
					}
					return false;
				}

				public void scan(File file) {}

				public boolean acceptResult(String fqn) {
					return false;
				}
			});
			
			AtomAndObject[] result = new AtomAndObject[foundClassNames.size()];
			for(int i = 0; i < foundClassNames.size(); i++) {
				String cn = foundClassNames.get(i);
				try {
					AtomAndObject aao = new AtomAndObject(atom, createObjectFromClass(cn, atom));
					result[i] = aao;
				}
				catch(Throwable t) {
					log.error("Could not load object with class: " + cn, t);
				}
			}
			
			return result;
		}
		else {
			// it doesn't look like a regular expression, so probably just a single class
			try {
				Object o = createObjectFromClass(className, atom);
				
				return o;
			}
			catch(Throwable t) {
				log.error("Could not load object with class: " + atom.getValue(), t);
			}
		}
		return null;
	}
	
	/**
	 * Creates a bean based on a fully resolved class name and assigns the properties to it
	 * @param classname The class name of the bean to create
	 * @param atom The configuration atom potentially with property values
	 * @return
	 */
	protected Object createObjectFromClass(String className, ConfigurationAtom atom) throws Exception {
		Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
		
		if(c.isInterface() || c.isAnnotation() || c.isEnum() || c.isArray()) {
			return null;
		}
		Object o = c.newInstance();
		
		if(atom instanceof FocusAndPropertiesAtom) {
			assignProperties(o, ((FocusAndPropertiesAtom)atom).getProperties());
		}
		return o;
	}
	
	/**
	 * Assign the properties from the configuration.
	 * @param focus
	 * @param properties
	 */
	protected void assignProperties(Object focus, Map<String, String> properties) {
		ParameterMarshal marshal = new ParameterMarshal(conf);
		
		for(String propertyName : properties.keySet()) {
			Method m = ReflectionUtils.getSetterMethod(focus, propertyName);
			try {
				marshal.call(focus, m, true, "creation-properties", properties);
			}
			catch(Exception e) {
				log.error("Could not set configuration property: " + propertyName, e);
			}
		}
	}

}
