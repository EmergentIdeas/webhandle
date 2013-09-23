package com.emergentideas.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	static DateFormat httpDateFormat;
	static DateFormat html5DateFormat;
	static DateFormat html5DateTimeLocalFormat;
	static DateFormat html5WeekFormat;
	static DateFormat html5MonthFormat;
	static DateFormat html5TimeFormat;
	
	
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
	 * Returns the global copy of the HTML5 Date Format.  Don't change this one
	 * @return
	 */
	public static DateFormat html5DateFormat() {
		if(html5DateFormat == null) {
			html5DateFormat = newHtml5DateFormat();
		}
		return html5DateFormat;
	}
	
	/**
	 * Returns the global copy of the HTML5 Datetime-local Format.  Don't change this one
	 * @return
	 */
	public static DateFormat html5DateTimeLocalFormat() {
		if(html5DateTimeLocalFormat == null) {
			html5DateTimeLocalFormat = newHtml5DateTimeLocalFormat();
		}
		return html5DateTimeLocalFormat;
	}
	
	/**
	 * Returns the global copy of the HTML5 Week Format.  Don't change this one
	 * @return
	 */
	public static DateFormat html5WeekFormat() {
		if(html5WeekFormat == null) {
			html5WeekFormat = newHtml5WeekFormat();
		}
		return html5WeekFormat;
	}
	
	/**
	 * Returns the global copy of the HTML5 Week Format.  Don't change this one
	 * @return
	 */
	public static DateFormat html5MonthFormat() {
		if(html5MonthFormat == null) {
			html5MonthFormat = newHtml5MonthFormat();
		}
		return html5MonthFormat;
	}
	
	/**
	 * Returns the global copy of the HTML5 Time Format.  Don't change this one
	 * @return
	 */
	public static DateFormat html5TimeFormat() {
		if(html5TimeFormat == null) {
			html5TimeFormat = newHtml5TimeFormat();
		}
		return html5TimeFormat;
	}
	
	
	/**
	 * Returns a new copy of the http expires date format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtmlExpiresDateFormat() {
		return newDateFormat("EEE, dd MMM yyyy HH:mm:ss z", "GMT");
	}
	
	/**
	 * Returns a new copy of the html5 date format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtml5DateFormat() {
		return newDateFormat("yyyy-MM-dd");
	}
	
	/**
	 * Returns a new copy of the html5 datetime-local format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtml5DateTimeLocalFormat() {
		return newDateFormat("yyyy-MM-dd'T'HH:mm");
	}
	
	/**
	 * Returns a new copy of the html5 week format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtml5WeekFormat() {
		return newDateFormat("yyyy-'W'ww");
	}
	
	/**
	 * Returns a new copy of the html5 month format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtml5MonthFormat() {
		return newDateFormat("yyyy-MM");
	}
	
	/**
	 * Returns a new copy of the html5 time format.  Change this in any way you like.
	 * @return
	 */
	public static DateFormat newHtml5TimeFormat() {
		return newDateFormat("HH:mm");
	}
	
	
	public static DateFormat newDateFormat(String format) {
		return newDateFormat(format, null);
	}
	
	public static DateFormat newDateFormat(String format, String timeZone) {
		DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
		if(StringUtils.isNotBlank(timeZone)) {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return dateFormat;
	}

	
	public static Date combineDateAndTime(Date useForDate, Date useForTime)
	{
		Calendar cRet = Calendar.getInstance();
		cRet.setTime(useForDate);
		
		Calendar cTime = Calendar.getInstance();
		cTime.setTime(useForTime);
		cRet.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
		cRet.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
		cRet.set(Calendar.SECOND, cTime.get(Calendar.SECOND));
		cRet.set(Calendar.MILLISECOND, cTime.get(Calendar.MILLISECOND));
		
		return(cRet.getTime());
	}


}
