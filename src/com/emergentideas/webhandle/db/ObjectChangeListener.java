package com.emergentideas.webhandle.db;

public interface ObjectChangeListener<T> {

	public enum ChangeType { PRE_UPDATE, POST_UPDATE, PRE_PERSIST, POST_PERSIST, PRE_REMOVE, POST_REMOVE, POST_LOAD }
	
	public ChangeType[] listenFor();
	
	public void changeEvent(ChangeType type, T t);
}
