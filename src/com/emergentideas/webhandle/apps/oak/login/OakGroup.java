package com.emergentideas.webhandle.apps.oak.login;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OakGroup {

	@Id
	protected String groupName;
	
	public OakGroup() {}
	
	public OakGroup(String groupName) {
		setGroupName(groupName);
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
