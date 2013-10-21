package com.emergentideas.webhandle.json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.emergentideas.utils.ReflectionUtils;
import com.emergentideas.webhandle.output.SegmentedOutput;


public abstract class DateSerializerBase<T> implements ObjectSerializer<T> {

	protected String format = "yyyy-mm-dd'T'HH:MM:ss.SSS'Z'";
	protected SimpleDateFormat jsonDateFormatter =  new SimpleDateFormat(format); 
	
	public DateSerializerBase() {
		jsonDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
	}
	
	
	protected void innerSerialize(SegmentedOutput output, Date objToSerialize, String... allowedSerializationProfiles) {
		boolean asString = ReflectionUtils.contains(allowedSerializationProfiles, "date-as-string");
		StringBuilder sb = output.getStream("body");
		if(!asString) {
			sb.append("new Date(");
		}
		sb.append('"' + jsonDateFormatter.format(objToSerialize) + '"');
		if(!asString) {
			sb.append(")");
		}
	}


	public SimpleDateFormat getJsonDateFormatter() {
		return jsonDateFormatter;
	}

	public void setJsonDateFormatter(SimpleDateFormat jsonDateFormatter) {
		this.jsonDateFormatter = jsonDateFormatter;
	}
}
