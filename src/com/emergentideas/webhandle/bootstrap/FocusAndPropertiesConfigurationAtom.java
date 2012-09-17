package com.emergentideas.webhandle.bootstrap;

public class FocusAndPropertiesConfigurationAtom extends
		PropertiesConfigurationAtom implements FocusAndPropertiesAtom {
	
	protected String focus;

	public String getFocus() {
		return focus;
	}
	
	public void setFocus(String focus) {
		this.focus = focus;
	}

}
