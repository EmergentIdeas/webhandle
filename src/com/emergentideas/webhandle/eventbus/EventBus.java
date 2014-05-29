package com.emergentideas.webhandle.eventbus;

public interface EventBus {

	public void register(Object listener);
	
	public void emit(String queue, Object event);
	
	public void unregister(Object listener);
}
