package com.emergentideas.webhandle.apps.oak.login;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.utils.CryptoUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.ParmManipulator;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

@RolesAllowed("administrators")
public class UserManagementHandle {

	protected AuthenticationService authenticationService;
	
	@Handle("/groups")
	@Template
	@Wrap("app_page")
	public String groups(Location location) {
		TableDataModel table = new TableDataModel()
			.setHeaders("Group Name")
			.setProperties("groupName")
			.setDeleteURLPattern("/groups/", "groupName", "/delete")
			.setEditURLPattern(0, "/groups/", "groupName", "/info")
			.setCreateNewURL("/groups/new");
		for(String groupName : authenticationService.getGroupNames()) {
			table.addItem(new OakGroup(groupName));
		}
		location.put("groups", table);
		
		return "login/groups";
	}
	
	@Handle(value = "/groups/new", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public String newGroupGet() {
		return "login/newGroup";
	}
	
	@Handle(value = "/groups/new", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object newGroupPost(Location location, RequestMessages message, String groupName) {
		if(authenticationService.doesGroupExist(groupName) == true) {
			message.getErrorMessages().add("A group with that name already exists.  Please select another.");
			location.put("groupName", groupName);
			return "login/newGroup";
		}
		
		authenticationService.createGroup(groupName);
		return new Show("/groups");
	}
	
	@Handle(value = "/groups/{groupName}/delete", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object deleteGroup(String groupName) {
		if(authenticationService.doesGroupExist(groupName)) {
			authenticationService.deleteGroup(groupName);
		}
		return new Show("/groups");
	}
	
	@Handle(value = "/groups/{groupName}/info", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object showGroup(Location location, String groupName) {
		location.put("groupName", groupName);
		location.put("userNames", authenticationService.getProfilesInGroup(groupName));
		return "login/groupDetails";
	}
	
	@Handle("/users")
	@Template
	@Wrap("app_page")
	public String users(Location location) {
		TableDataModel table = new TableDataModel()
			.setHeaders("Profile Name", "Full Name", "Email")
			.setProperties("profileName", "fullName", "email")
			.setDeleteURLPattern("/users/", "profileName", "/delete")
			.setEditURLPattern(0, "/users/", "profileName", "/info")
			.setCreateNewURL("/users/new")
			.setItems(authenticationService.getUsers().toArray());
		location.put("users", table);
		
		return "login/users";
	}
	
	@Handle(value = "/users/{profileName}/info", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object editUserGet(Location location, String profileName) {
		User found = authenticationService.getUserByProfileName(profileName);
		if(found == null) {
			return new Show("/users");
		}
		
		location.add(found);
		location.put("groupNames", authenticationService.getGroupNames());
		location.put("assignedGroups", new ArrayList<String>(found.getGroupNames()));
		return "login/editUser";
	}

	@Handle(value = "/users/{profileName}/info", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object editUserPost(Location location, RequestMessages messages, 
			String profileName, String fullName, String email, String newPassword, @NotNull(String.class) List<String> assignedGroups) {
		
		User found = authenticationService.getUserByProfileName(profileName);
		if(found == null) {
			return new Show("/users");
		}
		
		authenticationService.setFullName(profileName, fullName);
		authenticationService.setEmail(profileName, email);
		
		if(StringUtils.isBlank(newPassword) == false) {
			authenticationService.setPassword(profileName, newPassword);
		}
		
		Collection<String> existingGroups = found.getGroupNames();
		for(String group : existingGroups) {
			if(assignedGroups.contains(group) == false) {
				authenticationService.removeMember(group, profileName);
			}
		}
		
		for(String group : assignedGroups) {
			if(existingGroups.contains(assignedGroups) == false) {
				authenticationService.addMember(group, profileName);
			}
		}
		
		
		
		return new Show("/users");
	}
	
	@Handle(value = "/users/{profileName}/delete", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object deleteUser(String profileName) { 
		authenticationService.deleteUser(profileName);
		return new Show("/users");
	}
	
	
	@Handle(value = "/users/new", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object newUserPost(Location location, RequestMessages messages, ParmManipulator manip, 
			String newPassword, String profileName, String fullName, String email, @NotNull(String.class) List<String> assignedGroups) {
		
		if(StringUtils.isBlank(profileName)) {
			messages.getErrorMessages().add("The profile name must not be blank.");
			manip.addRequestParameters(location);
			return newUserGet(location);
		}
		
		if(authenticationService.getUserByProfileName(profileName) != null) {
			messages.getErrorMessages().add("A user by that name already exists.");
			manip.addRequestParameters(location);
			return newUserGet(location);
		}
		
		if(StringUtils.isBlank(newPassword)) {
			newPassword = CryptoUtils.genNewPassword(12);
		}
		
		authenticationService.createUser(profileName, email, newPassword);
		if(StringUtils.isBlank(fullName) == false) {
			authenticationService.setFullName(profileName, fullName);
		}
		
		for(String group : assignedGroups) {
			authenticationService.addMember(group, profileName);
		}
		return new Show("/users");
	}
	
	@Handle(value = "/users/new", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public String newUserGet(Location location) {
		location.put("groupNames", authenticationService.getGroupNames());
		return "login/newUser";
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	@Wire
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	
}
