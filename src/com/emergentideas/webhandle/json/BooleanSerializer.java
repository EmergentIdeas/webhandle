package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class BooleanSerializer implements ObjectSerializer<Boolean> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Boolean objToSerialize, String... allowedSerializationProfiles) {
		output.getStream("body").append(objToSerialize.toString());
	}

}
