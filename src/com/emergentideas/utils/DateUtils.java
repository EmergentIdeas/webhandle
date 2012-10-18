package com.emergentideas.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
	static DateFormat httpDateFormat;
	
	/**
	 * Returns the global copy of the HTML Expires Date Format.  Don't change this one
	 * @return
	 */
	public static DateFormat htmlExpiresDateFormat() {
		if(httpDateFormat == null){
			httpDateFormat = newHtmlExpiresDateFormat();
		}
		return httpDateFormat;
	}
	
	/**
	 * Returns a new copy of the http expires date format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtmlExpiresDateFormat() {
		DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return httpDateFormat;
	}

}
