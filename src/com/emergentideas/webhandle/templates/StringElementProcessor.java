package com.emergentideas.webhandle.templates;


import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class StringElementProcessor implements ElementProcessor {

	public boolean process(Location location, SegmentedOutput output,
			Element element, String elementSourceName, String... processingHints) {
		if(element instanceof StringElement) {
			output.getStream(elementSourceName).append(element.toString());
			
			return true;
		}
		
		return false;
	}

}
