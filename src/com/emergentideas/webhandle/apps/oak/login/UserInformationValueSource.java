package com.emergentideas.webhandle.apps.oak.login;

import java.util.Collection;

import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueSource;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

public class UserInformationValueSource implements ValueSource<Object> {
	
	protected User user;
	
	public static final String USER_ROLES_NAME = "userRoles";
	public static final String USER_PROFILE_NAME = "userProfile";
	public static final String USER_ID_NAME = "userId";
	
	public UserInformationValueSource(User user) {
		this.user = user;
	}

	public <T> Object get(String name, Class<T> type, InvocationContext context) {
		if(user == null) {
			return null;
		}
		if(USER_ROLES_NAME.equals(name)) {
			Collection<String> groups = user.getGroupNames();
			return groups.toArray(new String[groups.size()]);
		}
		if(USER_PROFILE_NAME.equals(name)) {
			return user.getProfileName();
		}
		if(USER_ID_NAME.equals(name)) {
			return user.getId();
		}
		if(User.class.isAssignableFrom(type)) {
			return user;
		}
		
		return null;
	}

	public <T> boolean canGet(String name, Class<T> type,
			InvocationContext context) {
		if(user == null) {
			return false;
		}
		
		if(USER_ROLES_NAME.equals(name) || USER_PROFILE_NAME.equals(name) || USER_ID_NAME.equals(name)) {
			return true;
		}
		if(User.class.isAssignableFrom(type)) {
			return true;
		}
		
		return false;
	}

	public boolean isCachable() {
		return true;
	}

	
}
