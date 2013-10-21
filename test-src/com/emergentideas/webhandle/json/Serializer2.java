package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"default", "one"})
public class Serializer2 implements ObjectSerializer<Number> {

	@Override
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Number objToSerialize, String... allowedSerializationProfiles) {
		

	}

}
