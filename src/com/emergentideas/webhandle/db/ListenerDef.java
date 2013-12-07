package com.emergentideas.webhandle.db;

import java.lang.reflect.Method;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType;

class ListenerDef {
	protected ObjectChangeListener listener;
	protected Class objectType;
	protected ChangeType[] allowedEvents;
	
	public ListenerDef(ObjectChangeListener listener) {
		this.listener = listener;
		Method m = ReflectionUtils.getFirstMethod(listener.getClass(), "changeEvent");
		objectType = m.getParameterTypes()[1];
		allowedEvents = listener.listenFor();
	}
	
	public void notify(ChangeType type, Object o) {
		listener.changeEvent(type, o);
	}
	
	public boolean shouldNotify(ChangeType type, Object o) {
		return objectType.isAssignableFrom(o.getClass()) && ReflectionUtils.contains(allowedEvents, type);
	}
	
	public ObjectChangeListener getListener() {
		return listener;
	}

	public void setListener(ObjectChangeListener listener) {
		this.listener = listener;
	}

	public Class getObjectType() {
		return objectType;
	}

	public void setObjectType(Class objectType) {
		this.objectType = objectType;
	}

	public ChangeType[] getAllowedEvents() {
		return allowedEvents;
	}

	public void setAllowedEvents(ChangeType[] allowedEvents) {
		this.allowedEvents = allowedEvents;
	}
	
	
}
