package com.emergentideas.webhandle;

import com.emergentideas.webhandle.templates.TemplateSource;

public class WebAppLocation {
	
	protected Location location;
	
	public WebAppLocation(Location location) {
		this.location = location;
		
		
		
	}
	
	public WebAppLocation init() {
		if(location.get("servicesByType") == null) {
			location.put("servicesByType", new AppLocation());
			location.put("servicesByName", new AppLocation());

		}
		return this;
	}
	
	public void setTemplateSource(TemplateSource source) {
		((Location)location.get("servicesByType")).put(TemplateSource.class.getName(), source);
	}

	public TemplateSource getTemplateSource() {
		return getTemplateSource(location);
	}
	
	public static TemplateSource getTemplateSource(Location location) {
		return (TemplateSource)location.get("servicesByType/" + TemplateSource.class.getName());
	}
	
	public void setParameterMarshalConfiguration(ParameterMarshalConfiguration config) {
		((Location)location.get("servicesByType")).put(ParameterMarshalConfiguration.class.getName(), config);
	}

	public ParameterMarshalConfiguration getParameterMarshalConfiguration() {
		return (ParameterMarshalConfiguration)location.get("servicesByType/" + ParameterMarshalConfiguration.class.getName());
	}

}
