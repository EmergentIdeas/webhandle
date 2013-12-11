package com.emergentideas.webhandle.db;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType;

public class JPAGlobalListener {
	
	protected static ObjectChangeListener listener;
	
	protected ObjectChangeListener objListener;
	
	public JPAGlobalListener() {
		this.objListener = listener;
	}
	
	@PreUpdate
	public void preUpdate(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.PRE_UPDATE, o);
		}
	}
	
	@PostUpdate
	public void postUpdate(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.POST_UPDATE, o);
		}
	}
	
	@PrePersist
	public void prePersist(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.PRE_PERSIST, o);
		}
	}
	
	@PostPersist
	public void postPersist(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.POST_PERSIST, o);
		}
	}
	
	@PreRemove
	public void preRemove(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.PRE_REMOVE, o);
		}
	}
	
	@PostRemove
	public void postRemove(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.POST_REMOVE, o);
		}
	}
	
	@PostLoad
	public void postLoad(Object o) {
		if(getListener() != null) {
			getListener().changeEvent(ChangeType.POST_LOAD, o);
		}
	}

	public static ObjectChangeListener getStaticListener() {
		return listener;
	}

	public ObjectChangeListener getListener() {
		return objListener;
	}

	public static void setListener(ObjectChangeListener listener) {
		JPAGlobalListener.listener = listener;
	}
}
