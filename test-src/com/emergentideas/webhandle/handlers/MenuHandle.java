package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class MenuHandle {

	@Handle("/menu")
	@Template
	@Wrap("app_page")
	public String menu() {
		return "menu";
	}
}
