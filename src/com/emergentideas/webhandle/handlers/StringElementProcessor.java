package com.emergentideas.webhandle.handlers;

import java.util.regex.Pattern;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.Element;
import com.emergentideas.webhandle.templates.ElementProcessor;
import com.emergentideas.webhandle.templates.StringElement;

public class StringElementProcessor implements ElementProcessor {

	public boolean process(Location location, SegmentedOutput output, Element element) {
		if(element instanceof StringElement) {
			UrlRegexOutput urlOut = new UrlRegexOutput(output);
			
			String s = Pattern.quote(element.toString());
			urlOut.getRegexString().append(s);
			
			return true;
		}
		
		return false;
	}

}
