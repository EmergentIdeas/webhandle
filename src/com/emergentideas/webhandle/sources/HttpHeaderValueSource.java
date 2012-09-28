package com.emergentideas.webhandle.sources;

import javax.servlet.http.HttpServletRequest;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;

public class HttpHeaderValueSource implements ValueSource<Object> {

	HttpServletRequest request;
	
	public HttpHeaderValueSource(HttpServletRequest request) {
		this.request = request;
	}
	
	public <T extends Object> String[] get(String name, Class<T> type,
			InvocationContext context) {
		String value = request.getHeader(name);
		return new String[] { value };
	}



	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		String value = request.getHeader(name);
		if(value != null) {
			return true;
		}
		return false;
	}

	public boolean isCachable() {
		return true;
	}

	
}
