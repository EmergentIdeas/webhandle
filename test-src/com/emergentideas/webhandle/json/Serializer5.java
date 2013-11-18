package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"default", "two"})
public class Serializer5 implements ObjectSerializer<byte[]> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			byte[] objToSerialize, String... allowedSerializationProfiles) {
		

	}

}
