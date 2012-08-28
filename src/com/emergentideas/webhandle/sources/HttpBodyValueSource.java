package com.emergentideas.webhandle.sources;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;

public class HttpBodyValueSource implements ValueSource<String[]> {

	HttpServletRequest request;
	
	public HttpBodyValueSource(HttpServletRequest request) {
		this.request = request;
	}
	
	public <T> String[] get(String name, Class<T> type,
			InvocationContext context) {
		String[] values = request.getParameterValues(name);
		return values;
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		String[] values = request.getParameterValues(name);
		if(values != null) {
			return true;
		}
		return false;
	}

	public boolean isCachable() {
		return true;
	}

	
}
