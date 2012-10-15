package com.emergentideas.webhandle.apps.oak.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.emergentideas.webhandle.NoInject;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

@Entity
public class OakUser implements User {

	@Id
	@GeneratedValue
	protected int id;
	
	protected String profileName;
	protected String fullName;
	protected String email;
	protected boolean active;
	protected String authenticationSystem = AuthenticationService.LOCAL_AUTHENTICATION_SYSTEM;
	
	@ManyToMany
	protected List<OakGroup> groups = new ArrayList<OakGroup>();
	
	
	public String getProfileName() {
		return profileName;
	}

	public String getFullName() {
		return fullName;
	}

	
	public Collection<String> getGroupNames() {
		List<String> names = new ArrayList<String>();
		for(OakGroup g : groups) {
			names.add(g.getGroupName());
		}
		return names;
	}
	
	public List<OakGroup> getGroups() {
		return groups;
	}

	public String getId() {
		return id + "";
	}

	@NoInject
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAuthenticationSystem() {
		return authenticationSystem;
	}

	public void setAuthenticationSystem(String authenticationSystem) {
		this.authenticationSystem = authenticationSystem;
	}

	
}
