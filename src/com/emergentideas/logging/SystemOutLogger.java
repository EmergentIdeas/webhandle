package com.emergentideas.logging;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SystemOutLogger extends LoggerBase {

	protected static Map<String, Logger> createdLoggers = Collections.synchronizedMap(new HashMap<String, Logger>());
	protected String locationPrefix;
	
	public SystemOutLogger() {
		
	}
	
	public SystemOutLogger(@SuppressWarnings("rawtypes") Class c) {
		locationPrefix = createLoggerName(c);
	}
	
	/**
	 * Returns a logger, possibly an already existing logger, for this class.
	 * @param c
	 * @return
	 */
	public synchronized static Logger get(@SuppressWarnings("rawtypes") Class c) {
		String name = createLoggerName(c);
		Logger log = createdLoggers.get(name);
		if(log == null) {
			log = new SystemOutLogger(c);
			createdLoggers.put(name, log);
		}
		return log;
	}
	
	/**
	 * Returns the classes name or null if the class is null
	 * @param c
	 * @return
	 */
	protected static String createLoggerName(Class c) {
		if(c != null) {
			return c.getName();
		}
		return null;
	}
	@Override
	public void error(String msg) {
		System.err.println(getPrecedentText("Error") + msg);
	}

	@Override
	public void warn(String msg) {
		System.out.println(getPrecedentText("Warn") + msg);
	}

	@Override
	public void info(String msg) {
		System.out.println(getPrecedentText("Info") + msg);
	}

	@Override
	public void debug(String msg) {
		System.out.println(getPrecedentText("Debug") + msg);
	}

	@Override
	public void error(String msg, Throwable t) {
		error(msg);
		t.printStackTrace(System.err);
	}

	@Override
	public void warn(String msg, Throwable t) {
		warn(msg);
		t.printStackTrace(System.out);
	}

	protected String getPrecedentText(String typeKeyword) {
		return typeKeyword + 
				(locationPrefix == null ? "" : "(from " + locationPrefix + ") " ) + 
				" (@ " + new Date().toString() + "): ";
	}

	public String getLocationPrefix() {
		return locationPrefix;
	}

	public void setLocationPrefix(String locationPrefix) {
		this.locationPrefix = locationPrefix;
	}
	
	

}
