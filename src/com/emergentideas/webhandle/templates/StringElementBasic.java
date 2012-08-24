package com.emergentideas.webhandle.templates;

public class StringElementBasic implements StringElement {

	protected String value;
	
	public StringElementBasic() {
		
	}
	
	public StringElementBasic(String value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
