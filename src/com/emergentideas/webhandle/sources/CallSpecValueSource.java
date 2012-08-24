package com.emergentideas.webhandle.sources;

import java.util.HashMap;

import com.emergentideas.webhandle.CallSpec;

public class CallSpecValueSource extends MapValueSource {
	
	public CallSpecValueSource(CallSpec spec) {
		super(spec.getCallSpecificProperties() == null ? new HashMap<String, Object>() : spec.getCallSpecificProperties());
		data.put("properties", data);
	}

	@Override
	public boolean isCachable() {
		return false;
	}

	
}
