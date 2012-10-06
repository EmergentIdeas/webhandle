package com.emergentideas.utils;

import org.apache.commons.jexl2.JexlContext;

public class ForFunctions {
	
	JexlContext context;
	
	public ForFunctions(JexlContext context) {
		this.context = context;
	}
	
	public String addA(String input) {
		return "a" + input;
	}

}
