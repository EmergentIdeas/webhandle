package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class NumberSerializer implements ObjectSerializer<Number> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Number objToSerialize, String... allowedSerializationProfiles) {
		if(objToSerialize != null) {
			output.getStream("body").append(objToSerialize.toString());
		}
	}

}
