package com.emergentideas.webhandle.output;

import java.util.List;
import java.util.Map;

/**
 * Captures the stream output for a single section so it can be post processed.  All
 * other calls it passes through to a parent {@link SegmentedOutput}.
 * @author kolz
 *
 */
public class SegmentedOutputOverlay extends SegmentedOutput {
	
	protected SegmentedOutput parent;
	protected String captureSection;
	
	public SegmentedOutputOverlay(SegmentedOutput parent, String captureSection) {
		this.parent = parent;
		this.captureSection = captureSection;
	}

	@Override
	public StringBuilder getStream(String name) {
		
		if(captureSection.equals(name)) {
			return super.getStream(name);
		}
		else {
			return parent.getStream(name);
		}
	}

	@Override
	public Map<String, String> getPropertySet(String name) {
		return parent.getPropertySet(name);
	}

	@Override
	public List<String> getList(String name) {
		return parent.getList(name);
	}
}
