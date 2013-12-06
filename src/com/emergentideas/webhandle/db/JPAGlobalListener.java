package com.emergentideas.webhandle.db;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class JPAGlobalListener {
	
	@PreUpdate
	public void preUpdate(Object o) {
		
	}
	
	@PostUpdate
	public void postUpdate(Object o) {
		
	}
	
	@PrePersist
	public void prePersist(Object o) {
		
	}
	
	@PostPersist
	public void postPersist(Object o) {
		
	}
	
	@PreRemove
	public void preRemove(Object o) {
		
	}
	
	@PostRemove
	public void postRemove(Object o) {
		
	}
	
	@PostLoad
	public void postLoad(Object o) {
		
	}
	

}
