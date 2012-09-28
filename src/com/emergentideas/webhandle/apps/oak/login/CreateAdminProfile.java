package com.emergentideas.webhandle.apps.oak.login;

import com.emergentideas.webhandle.Init;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;

public class CreateAdminProfile {
	
	AuthenticationService authenticationService;

	@Init
	public void init() {
		if(authenticationService.getUserByProfileName("administrator") == null) {
			authenticationService.createUser("administrator", "administrator", "passw0rd");
			authenticationService.createGroup("administrators");
			authenticationService.addMember("administrators", "administrator");
		}
	}
	
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	@Wire
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	

}
