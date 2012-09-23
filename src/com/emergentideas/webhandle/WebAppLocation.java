package com.emergentideas.webhandle;

import com.emergentideas.webhandle.templates.TemplateSource;

public class WebAppLocation {
	
	protected Location location;
	
	public static final String WEB_PARAMETER_MARSHAL_CONFIGURATION = "web-parameter-marshal-configuration";
	
	public WebAppLocation(Location location) {
		this.location = location;
		
		
		
	}
	
	protected WebAppLocation initIfNeeded() {
		if(location.get("servicesByType") == null) {
			init();
		}
		return this;
	}
	
	public WebAppLocation init() {
		location.put("servicesByType", new AppLocation());
		location.put("servicesByName", new AppLocation());
		return this;
	}
	
	public void setServiceByName(String name, Object service) {
		initIfNeeded();
		((Location)location.get("servicesByName")).put(name, service);
	}
	
	public void setServiceByType(String type, Object service) {
		initIfNeeded();
		((Location)location.get("servicesByType")).put(type, service);
	}
	
	public Object getServiceByType(String type) {
		return location.get("servicesByType/" + type);
	}
	
	public <T> T getServiceByType(Class<T> type) {
		Object o = getServiceByType(type.getName());
		if(o == null) {
			return null;
		}
		
		if(type.isAssignableFrom(o.getClass())) {
			return (T)o;
		}
		return null;
	}
	
	public Object getServiceByName(String name) {
		return location.get("servicesByName/" + name);
	}
	
	public void setTemplateSource(TemplateSource source) {
		setServiceByType(TemplateSource.class.getName(), source);
	}

	public TemplateSource getTemplateSource() {
		return (TemplateSource)getServiceByType(TemplateSource.class.getName());
	}
	
	public void setParameterMarshalConfiguration(ParameterMarshalConfiguration config) {
		setServiceByType(ParameterMarshalConfiguration.class.getName(), config);
	}

	public ParameterMarshalConfiguration getParameterMarshalConfiguration() {
		return (ParameterMarshalConfiguration)getServiceByType(ParameterMarshalConfiguration.class.getName());
	}

}
