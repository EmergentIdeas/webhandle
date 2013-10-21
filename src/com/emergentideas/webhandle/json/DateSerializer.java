package com.emergentideas.webhandle.json;

import java.util.Date;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"date-as-string", "default" })
public class DateSerializer extends DateSerializerBase<Date> {

	@Override
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Date objToSerialize, String... allowedSerializationProfiles) {
		innerSerialize(output, objToSerialize, allowedSerializationProfiles);
	}

	
}
