package com.emergentideas.webhandle.apps.oak.login;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OakGroup {

	@Id
	@GeneratedValue
	protected int id;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
