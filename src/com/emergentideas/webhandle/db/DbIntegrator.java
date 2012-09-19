package com.emergentideas.webhandle.db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.persistence.Entity;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.WebAppLocation;
import com.emergentideas.webhandle.assumptions.oak.AppLoader;
import com.emergentideas.webhandle.bootstrap.ConfigurationAtom;
import com.emergentideas.webhandle.bootstrap.Integrate;
import com.emergentideas.webhandle.bootstrap.Integrator;
import com.emergentideas.webhandle.bootstrap.Loader;

@Integrate
public class DbIntegrator implements Integrator {

	public void integrate(Loader loader, Location location,
			ConfigurationAtom atom, Object focus) {
		if(focus == null) {
			return;
		}
		
		WebAppLocation webApp = new WebAppLocation(location);
		Class c = null;
		
		if(focus instanceof DbConfiguration) {
			
			final DbConfiguration conf = (DbConfiguration)focus;
			ClassLoader oldClassLoader = (ClassLoader)webApp.getServiceByName(AppLoader.CLASS_LOADER_NAME);
			if(oldClassLoader == null) {
				oldClassLoader = Thread.currentThread().getContextClassLoader();
			}
			ClassLoader classloader = new ClassLoader(oldClassLoader) {
				@Override
				protected Enumeration<URL> findResources(String name)
						throws IOException {
					if("META-INF/persistence.xml".equals(name)) {
						final StringBuilder data = new StringBuilder("<persistence xmlns=\"http://java.sun.com/xml/ns/persistence\"\n" + 
								"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
								"	xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence persistence_1_0.xsd\"\n" + 
								"	version=\"1.0\">\n" + 
								"	<persistence-unit name=\"" + conf.getUnitName() + "\" transaction-type=\"RESOURCE_LOCAL\">\n" + 
								"		<provider>" + conf.getProvider() + "</provider>\n" );
						
						for(String className : conf.getClassNames()) {
							data.append("		<class>" + className + "</class>\n" );
						}
						data.append(
								"		<exclude-unlisted-classes>true</exclude-unlisted-classes>\n" + 
								"		<properties>\n" + 
								"			<property name=\"javax.persistence.jdbc.driver\" value=\"" + conf.getDriver() + "\" />\n" + 
								"			<property name=\"javax.persistence.jdbc.url\" value=\"" + conf.getUrl() + "\" />\n" + 
								"			<property name=\"javax.persistence.jdbc.user\" value=\"" + conf.getUser() + "\" />\n" + 
								"			<property name=\"javax.persistence.jdbc.password\" value=\"" + conf.getPassword() + "\" />\n");
						
						for(Entry<String, String> entry : conf.getArbitraryProperties().entrySet()) {
							data.append("			<property name=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>\n");
						}
								 
						data.append(		"		</properties>\n" + 
								"	</persistence-unit>\n" + 
								"</persistence>"
								);
						Vector<URL> urls = new Vector<URL>();
						urls.add(
								new URL("local", "localhost", 80, "/fakelocation/META-INF/persistence.xml", new URLStreamHandler() {
									
									@Override
									protected URLConnection openConnection(URL u) throws IOException {
										return new URLConnection(u) {
											@Override
											public void connect() throws IOException {
											}

											@Override
											public InputStream getInputStream()
													throws IOException {
												InputStream is = new ByteArrayInputStream(data.toString().getBytes());
												return is;
											}
										};
									}
								})
							);
						return urls.elements();
					}
					return super.findResources(name);
				}

			};
			Thread.currentThread().setContextClassLoader(classloader);
			webApp.setServiceByName(AppLoader.CLASS_LOADER_NAME, classloader);
			
			return;
		}
		else if(focus instanceof Class) {
			c = (Class)focus;
		}
		else {
			c = focus.getClass();
		}
		
		if(c != null) {
			Entity entity = ReflectionUtils.getAnnotationOnClass(c, Entity.class);
			if(entity != null) {
				DbConfiguration conf = (DbConfiguration)webApp.getServiceByName("dbConfiguration");
				if(conf != null) {
					conf.getClassNames().add(c.getName());
				}
			}
		}

	}

}
