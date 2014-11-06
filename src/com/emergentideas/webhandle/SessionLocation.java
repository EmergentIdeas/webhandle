package com.emergentideas.webhandle;

import javax.servlet.http.HttpServletRequest;

public class SessionLocation extends AppLocation {

	protected boolean saved = false;
	protected String locationKey;
	protected HttpServletRequest request;
	
	
	public SessionLocation(String locationKey, HttpServletRequest request, Location parent) {
		super(parent);
		this.locationKey = locationKey;
		this.request = request;
		super.put(Constants.SESSION_LOCATION, this);
	}


	@Override
	public void put(String key, Object value) {
		saveIfNeeded();
		super.put(key, value);
	}


	@Override
	public void add(Object current) {
		saveIfNeeded();
		super.add(current);
	}
	
	protected void saveIfNeeded() {
		if(saved == false) {
			saved = true;
			request.getSession().setAttribute(locationKey, this);
		}
	}
}
