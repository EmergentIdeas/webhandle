package com.emergentideas.webhandle.json;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"default", "two"})
public class Serializer4 implements ObjectSerializer<Integer> {

	@Override
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Integer objToSerialize, String... allowedSerializationProfiles) {
		

	}

}
