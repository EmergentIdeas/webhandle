package com.emergentideas.webhandle.json;

import java.io.InputStream;

import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.SegmentedOutput;

public class JsonRespondent extends DirectRespondent {

	protected Serializer serializer;
	protected Object objToSerialize;
	protected String[] serializationProfiles;
	
	public JsonRespondent(Serializer serializer, Object objToSerialize, String ... serializationProfiles) {
		super();
		this.serializer = serializer;
		this.objToSerialize = objToSerialize;
		this.serializationProfiles = serializationProfiles;
		addCacheHeaders(0);
		headers.put("Content-Type", "application/json");
		responseStatus = 200;
	}

	@Override
	protected InputStream transformUserObjectToInputStream(Object useObject) {
		SegmentedOutput so = new SegmentedOutput();
		try {
			serializer.serialize(so, objToSerialize, serializationProfiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.transformUserObjectToInputStream(so.getStream("body").toString());
	}
	
	
	
	
	
}
