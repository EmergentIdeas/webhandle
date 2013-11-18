package com.emergentideas.webhandle.json;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.output.SegmentedOutput;

@JSONSerializer
public class StringSerializer implements ObjectSerializer<String> {

	public void serialize(Serializer callingSerializer, SegmentedOutput output,
			String objToSerialize, String... allowedSerializationProfiles) {
		if(objToSerialize != null) {
			output.getStream("body").append("\"" + StringEscapeUtils.escapeJavaScript(objToSerialize) + "\"");
		}
	}

}
