package com.emergentideas.webhandle.handlers;

import com.emergentideas.webhandle.templates.TripartateParser;

public class UrlRequestParser extends TripartateParser {
	
	public UrlRequestParser() {
		initiatingSequence = "{";
		handlingSeparator = ":";
		terminatingSequence = "}";
	}

}
