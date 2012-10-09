package com.emergentideas.webhandle.apps.oak.login;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Type;
import com.emergentideas.webhandle.Wire;
import com.emergentideas.webhandle.assumptions.oak.Constants;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.interfaces.AuthenticationService;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

@Type("com.emergentideas.webhandle.apps.oak.login.LoginHandle")
public class LoginHandle {
	
	protected String successURL = "menu";
	
	protected AuthenticationService authenticationService;
	
	@Handle(value = "/login", method = HttpMethod.GET)
	@Template
	@Wrap("public_page")
	public Object loginGet(Location location, String forward) {
		if(StringUtils.isBlank(forward) == false) {
			location.put("forward", forward);
		}
		
		return "login/login";
	}
	
	@Handle(value = "/login", method = HttpMethod.POST)
	@Template
	@Wrap("public_page")
	public Object loginPost(Location location, RequestMessages messages, String forward, String userName, String password) {
		
		if(isValidLogin(userName, password) == false) {
			location.put("userName", userName);
			messages.getErrorMessages().add("A user with that name and password could not be found.");
			return "login/login";
		}
		
		// adds the User object to the session location
		User user = authenticationService.getUserByProfileName(userName);
		((Location)location.get(Constants.SESSION_LOCATION)).put(Constants.CURRENT_USER_OBJECT, user);
		
		// loading the object so it doesn't have a lazy init exception
		user.getGroupNames();
		
		if(StringUtils.isBlank(forward) == false) {
			return new Show(forward);
		}
		else {
			return new Show(successURL);
		}
	}
	
	@Handle("/logout")
	@Template
	@Wrap("public_page")
	public Object logout(Location location, RequestMessages messages) {
		messages.getSuccessMessages().add("You have logged out.  Hope to see you again soon.");
		((Location)location.get(Constants.SESSION_LOCATION)).put(Constants.CURRENT_USER_OBJECT, null);
		return "login/login";
	}

	protected boolean isValidLogin(String userName, String password) {
		return authenticationService.isAuthenticated(userName, password);
	}

	public String getSuccessURL() {
		return successURL;
	}

	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	@Wire
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	
}
