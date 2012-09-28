package com.emergentideas.webhandle.apps.oak.login;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.ParameterMarshal;
import com.emergentideas.webhandle.PreRequest;
import com.emergentideas.webhandle.ValueSource;
import com.emergentideas.webhandle.assumptions.oak.Constants;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

public class UserInformationSourceInterceptor {
	
	@PreRequest
	public void setupUserInformationSource(ParameterMarshal marshal, Location location) {
		User user = (User)location.get(Constants.CURRENT_USER_OBJECT);
		ValueSource<?> vs = new UserInformationValueSource(user);
		marshal.addSource(Constants.USER_INFORMATION_SOURCE_NAME, vs);
	}

}
