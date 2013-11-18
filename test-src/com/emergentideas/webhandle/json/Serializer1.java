package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"default", "one"})
public class Serializer1 implements ObjectSerializer<String> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			String objToSerialize, String... allowedSerializationProfiles) {
		

	}

}
