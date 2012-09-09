package com.emergentideas.webhandle;

public class NamedServicesValueSource implements ValueSource<Object> {

	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		WebAppLocation webApp = new WebAppLocation(context.getFoundParameter(Location.class));
		return webApp.getServiceByName(name);
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		return get(name, type, context) != null;
	}

	public boolean isCachable() {
		return false;
	}

	
}
