package com.emergentideas.webhandle.transformers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.pojava.datetime.DateTime;

import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.ValueTransformer;

public class StringToDateTransformer implements ValueTransformer<String, String, Date[]> {

	// This is also the HTML5 date format
	protected static final String sWidgetDateFormat = "yyyy-MM-dd";
	
	protected static final String sWidgetDateFormat2 = "yyyy.MM.dd";
	protected static final String sWidgetDateFormat3 = "MM/dd/yyyy";
	protected static final String sWidgetDateFormat4 = "dd/MM/yyyy";
	
	protected static final String sWidgetTimeFormat = "HH:mm:ss";
	protected static final String sWidgetTimeFormat2 = "H:mm:ss";
	
	// This is also the HTML5 time format
	protected static final String sWidgetTimeFormat3 = "HH:mm";
	
	protected static final String sWidgetTimeFormat4 = "H:mm";
	
	protected static final String sWidgetTimeFormat5 = "h:mma";
	protected static final String sWidgetTimeFormat6 = "h:mm a";
	
	protected static final String sWholeFormat = "yyyy-MM-dd HH:mm:ss";
	protected static final String sWholeFormat2 = "yyyy.MM.dd HH:mm:ss";
	
	protected DateFormat dfDateAndTime = new SimpleDateFormat(sWholeFormat);
	protected DateFormat dfDateAndTime2 = new SimpleDateFormat(sWholeFormat2);
	protected DateFormat dfDateAndTime3 = DateUtils.newHtml5DateTimeLocalFormat();
	
	protected DateFormat dfDateOnly = new SimpleDateFormat(sWidgetDateFormat);
	protected DateFormat dfDateOnly2 = new SimpleDateFormat(sWidgetDateFormat2);
	protected DateFormat dfDateOnly3 = new SimpleDateFormat(sWidgetDateFormat3);
	protected DateFormat dfDateOnly4 = new SimpleDateFormat(sWidgetDateFormat4);
	
	protected DateFormat dfTimeOnly1 = new SimpleDateFormat(sWidgetTimeFormat);
	protected DateFormat dfTimeOnly2 = new SimpleDateFormat(sWidgetTimeFormat2);
	protected DateFormat dfTimeOnly3 = new SimpleDateFormat(sWidgetTimeFormat3);
	protected DateFormat dfTimeOnly4 = new SimpleDateFormat(sWidgetTimeFormat4);
	protected DateFormat dfTimeOnly5 = new SimpleDateFormat(sWidgetTimeFormat5);
	protected DateFormat dfTimeOnly6 = new SimpleDateFormat(sWidgetTimeFormat6);
	
	protected DateFormat dfMonth = DateUtils.newHtml5MonthFormat();
	protected DateFormat dfWeek = DateUtils.newHtml5WeekFormat();

	protected DateFormat[] allFormats = {
			dfDateAndTime,
			dfDateAndTime2,
			dfDateAndTime3,
			dfDateOnly,
			dfDateOnly2,
			dfDateOnly3,
			dfDateOnly4,
			dfTimeOnly1,
			dfTimeOnly2,
			dfTimeOnly3,
			dfTimeOnly4,
			dfTimeOnly5,
			dfTimeOnly6,
			dfMonth,
			dfWeek
	};
	
	public Date[] transform(InvocationContext context, Map<String, String> transformationProperties, 
			Class finalParameterClass, String parameterName, String... source) {
		Date[] result = new Date[source.length];
		
		
		for(int i = 0; i < source.length; i++) {
			if(StringUtils.isBlank(source[i])) {
				continue;
			}
			result[i] = convert(source[i]);
			if(result[i] == null) {
				try {
					result[i] = DateTime.parse(source[i]).toDate();
				} catch(Exception e) {
					// Our shnifty date time converter couldn't do it either
				}
			}
		}
		
		return result;
	}
	
	protected Date convert(String d) {
		for(DateFormat format : allFormats) {
			try {
				return format.parse(d);
			}
			catch(Exception e) {
				// This is fine, it just means the format we tried won't work
			}
		}
		return null;
	}

	
}
