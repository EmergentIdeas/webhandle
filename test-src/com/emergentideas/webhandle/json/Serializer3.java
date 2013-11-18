package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"default", "two"})
public class Serializer3 implements ObjectSerializer<Object> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Object objToSerialize, String... allowedSerializationProfiles) {
		

	}

}
