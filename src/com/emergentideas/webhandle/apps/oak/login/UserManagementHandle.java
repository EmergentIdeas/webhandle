package com.emergentideas.webhandle.apps.oak.login;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class UserManagementHandle {

	@Handle("/groups")
	@Template
	@Wrap("app_page")
	public String groups(Location location) {
		TableDataModel table = new TableDataModel()
			.setHeaders("Group Name")
			.setProperties("groupName")
			.addItem(new OakGroup("one"))
			.addItem(new OakGroup("two"));
		location.put("groups", table);
		
		return "groups";
	}
}
