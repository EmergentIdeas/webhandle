package com.emergentideas.webhandle.json;

import java.util.Calendar;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer({"date-as-string", "default" })
public class CalendarSerializer extends DateSerializerBase<Calendar> {

	@Override
	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			Calendar objToSerialize, String... allowedSerializationProfiles) {
		innerSerialize(output, objToSerialize.getTime(), allowedSerializationProfiles);
	}

	
}
