package com.emergentideas.webhandle.apps.oak.login;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;

public class UserManagementHandle {

	protected AuthenticationService authenticationService;
	
	@Handle("/groups")
	@Template
	@Wrap("app_page")
	public String groups(Location location) {
		TableDataModel table = new TableDataModel()
			.setHeaders("Group Name")
			.setProperties("groupName");
		for(String groupName : authenticationService.getGroupNames()) {
			table.addItem(new OakGroup(groupName));
		}
		location.put("groups", table);
		
		return "groups";
	}
	
	@Handle(value = "/groups/new", method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public String newGroupGet() {
		return "newGroup";
	}
	
	@Handle(value = "/groups/new", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object newGroupPost(Location location, RequestMessages message, String groupName) {
		if(authenticationService.doesGroupExist(groupName) == true) {
			message.getErrorMessages().add("A group with that name already exists.  Please select another.");
			location.put("groupName", groupName);
			return "newGroup";
		}
		
		authenticationService.createGroup(groupName);
		return new Show("/groups");
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	@Wire
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	
}
