package com.emergentideas.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;

import com.emergentideas.webhandle.Location;

public class BindingUtils 
{
	public static Pattern pNameAttr = Pattern.compile("(?i)(\\sname=[\"](.*?)[\"])", Pattern.CASE_INSENSITIVE);
	public static Pattern pTextArea = Pattern.compile("(?i)(<textarea (.*?)>)(.*?)(</textarea>)", Pattern.CASE_INSENSITIVE);
	public static Pattern pValAttr = Pattern.compile("(?i)(\\svalue=[\"].*?[\"])", Pattern.CASE_INSENSITIVE);
	public static Pattern pInput = Pattern.compile("(?i)(<input.*?>)", Pattern.CASE_INSENSITIVE);
	public static Pattern pOption = Pattern.compile("(?i)<option.*?(/>|>.*?</option>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	public static Pattern pSelect = Pattern.compile("(<select.*?>)(.*?)(</select>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	
	/**
	 * Returns true if the string is null or blank
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s)
	{
		if(s == null)
		{
			return(true);
		}
		
		if(s.trim().equals(""))
		{
			return(true);
		}
		
		return(false);
	}
	
	public static boolean areStringsEqual(String s1, String s2)
	{
		if(s1 == null && s2 == null)
		{
			return(true);
		}
		if(s1 == null || s2 == null)
		{
			return(false);
		}
		
		return(s1.equals(s2));
	}
	
	public static String prefixURLsWithContextRoot(String prefix, List<String> otherAllowedPrefixes, String content) {
		if(isBlank(prefix)) {
			return content;
		}
		
		List<String> replacements = new ArrayList<String>();
		
		String reg = "(action=\")(/[^/].*?)(\")";
		Pattern pat = Pattern.compile(reg);
		Matcher m = pat.matcher(content);
		addReplacements(m, prefix, otherAllowedPrefixes, replacements);
		
		reg = "(href=\")(/[^/].*?)(\")";
		pat = Pattern.compile(reg);
		m = pat.matcher(content);
		addReplacements(m, prefix, otherAllowedPrefixes, replacements);
		
		reg = "(src=\")(/[^/].*?)(\")";
		pat = Pattern.compile(reg);
		m = pat.matcher(content);
		addReplacements(m, prefix, otherAllowedPrefixes, replacements);
		
		return StringUtils.replaceString(content, replacements.toArray(new String[replacements.size()]));
	}
	
	protected static void addReplacements(Matcher m, String prefix, List<String> otherAllowedPrefixes, List<String> replacements) {
		while(m.find()) {
			String start = m.group(1);
			String url = m.group(2);
			String end = m.group(3);
			
			if(startsWithAny(url, prefix, otherAllowedPrefixes) == false) {
				url = prefix + url;
				replacements.add(m.group(0));
				replacements.add(start + url + end);
			}
		}
	}
	
	protected static boolean startsWithAny(String focus, String primaryPrefix, List<String> secondaryPrefixes) {
		
		if(focus.startsWith(primaryPrefix)) {
			return true;
		}
		
		if(secondaryPrefixes != null) {
			for(String secondary : secondaryPrefixes) {
				if(focus.startsWith(secondary)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static String addValuesToAllElementTypes(Location ds, String content) {
		String transformed = BindingUtils.addValuesToInputElements(ds, content);
		transformed = BindingUtils.addValuesToTextAreaElements(ds, transformed);
		transformed = BindingUtils.addValuesToSelectElements(ds, transformed);
		return transformed;
	}
	
	public static String addValuesToTextAreaElements(Location data, String text)
	{
		StringBuffer sb = new StringBuffer();
		int startingPoint = 0;
		Matcher m = pTextArea.matcher(text);
		while(m.find())
		{
			sb.append(text.substring(startingPoint, m.start(1)));
			String s = text.substring(m.start(1), m.end(1));
			
			Matcher mName = pNameAttr.matcher(s);
			
			if(mName.find())
			{
				String nameOfInput = mName.group(2);
				
				Object o = data.get(nameOfInput);
				String valueToSet = o == null ? null : o.toString();
				
				sb.append(text.substring(m.start(1), m.end(1)));
				if(isBlank(valueToSet) == false)
				{
					sb.append(valueToSet);
				}
				sb.append("</textarea>");
			}
			else {
				sb.append(text);
			}
			startingPoint = m.end();
		}
		
		if(startingPoint < text.length()) {
			sb.append(text.substring(startingPoint, text.length()));
		}
		return sb.toString();
	}
	
	public static String addValuesToInputElements(Location data, String text)
	{
		StringBuffer sb = new StringBuffer();
		int startingPoint = 0;
		Matcher m = pInput.matcher(text);
		while(m.find())
		{
			sb.append(text.substring(startingPoint, m.start(1)));
			String s = text.substring(m.start(1), m.end(1));
			
			int valueAttrStart = -1;
			int valueAttrEnd = -1;
			
			int nameAttrStart = -1;
			int nameAttrEnd = -1;
			
			Matcher mName = pNameAttr.matcher(s);
			
			if(mName.find())
			{
				nameAttrStart = mName.start(1);
				nameAttrEnd = mName.end(1);
				
				String nameOfInput = mName.group(2);
				
				Object o = data.get(nameOfInput);
				String valueToSet = o == null ? null : o.toString();
				Date dateValue = null;
				
				if(o instanceof Date) {
					dateValue = (Date)o;
				}
				else if(o instanceof Calendar) {
					dateValue = ((Calendar)o).getTime();
				}
				
				if(isBlank(valueToSet) == false)
				{
					String inputType = getAttributeValue(s, "type");
					if("radio".equalsIgnoreCase(inputType)) {
						String buttonValue = getAttributeValue(s, "value");
						if(valueToSet.equals(buttonValue)) {
							s = addValueBeforeFirstElementTermination(s, "checked=\"checked\"");
						}
						else {
							s = StringUtils.replaceString(s, "checked=\"checked\"", "", "checked", "");
						}
					}
					else if("checkbox".equalsIgnoreCase(inputType)) {
						String buttonValue = getAttributeValue(s, "value");
						if(org.apache.commons.lang.StringUtils.isBlank(buttonValue)) {
							// if the button value is blank, then we're just looking for an indication that the check box should be
							// checked.  This indicates there is only one checkbox with this name.
							s = StringUtils.replaceString(s, "checked=\"checked\"", "", "checked", "");
							if(valueToSet.equalsIgnoreCase("on") || valueToSet.equalsIgnoreCase("yes") || valueToSet.equalsIgnoreCase("checked")) {
								s = addValueBeforeFirstElementTermination(s, "checked=\"checked\"");
							}
						}
						else {
							List values = data.all(nameOfInput);
							if(values.contains(buttonValue)) {
								s = addValueBeforeFirstElementTermination(s, "checked=\"checked\"");
							}
						}
					}
					else if("file".equalsIgnoreCase(inputType) || "submit".equalsIgnoreCase(inputType)) {
						// don't do anything
					}
					else {
						// There are lots of new input types in html5. Most are just text fields that the browser
						// provides some tooling for. However, some of them, like dates, require the text to be
						// specially formatted. No matter what, we should add the value. If we understand the
						// type, we should correctly format the value
						
						
						//If it already has a value, delete the value
						Matcher mValue = pValAttr.matcher(s);
						if(mValue.find())
						{
							valueAttrStart = mValue.start(1);
							valueAttrEnd = mValue.end(1);
							s = s.substring(0, valueAttrStart) + s.substring(valueAttrEnd);
							if(valueAttrStart < nameAttrStart) {
								// If the value comes before the name, change the location to adjust for the shorter string
								nameAttrStart -= (valueAttrEnd - valueAttrStart);
								nameAttrEnd -= (valueAttrEnd - valueAttrStart);
							}
						}
						
						if("text".equalsIgnoreCase(inputType) || "hidden".equalsIgnoreCase(inputType) || org.apache.commons.lang.StringUtils.isBlank(inputType)) {
							// This falls into a general text replacement category
						}
						else if("date".equalsIgnoreCase(inputType)) {
							if(dateValue != null) {
								valueToSet = DateUtils.html5DateFormat().format(dateValue);
							}
						}
						else if("datetime-local".equalsIgnoreCase(inputType) || "datetime".equalsIgnoreCase(inputType)) {
							if(dateValue != null) {
								valueToSet = DateUtils.html5DateTimeLocalFormat().format(dateValue);
							}
						}
						else if("month".equalsIgnoreCase(inputType)) {
							if(dateValue != null) {
								valueToSet = DateUtils.html5MonthFormat().format(dateValue);
							}
						}
						else if("week".equalsIgnoreCase(inputType)) {
							if(dateValue != null) {
								valueToSet = DateUtils.html5WeekFormat().format(dateValue);
							}
						}
						else if("time".equalsIgnoreCase(inputType)) {
							if(dateValue != null) {
								valueToSet = DateUtils.html5TimeFormat().format(dateValue);
							}
						}

						
						
						// escape the html
						valueToSet = StringEscapeUtils.escapeHtml(valueToSet);
						
						s = s.substring(0, nameAttrEnd ) + " value=\"" + valueToSet + "\" " + s.substring(nameAttrEnd);
					}
				}
			}
			
			sb.append(s);
			startingPoint = m.end(1);
		}
		
		if(startingPoint < text.length())
		{
			sb.append(text.substring(startingPoint));
		}
		
		return sb.toString();
	}
	
	public static String addValuesToSelectElements(Location data, String text)
	{
		StringBuffer sb = new StringBuffer();
		int startingPoint = 0;
		
		Matcher m = pSelect.matcher(text);
		while(m.find())
		{
			sb.append(text.substring(startingPoint, m.start()));
			String selectStart = m.group(1);
			String selectEnd = m.group(3);
			String nameOfInput = getAttributeValue(selectStart, "name");
			
			sb.append(selectStart);
			
			Matcher mOptions = pOption.matcher(m.group(2));
			
			while(mOptions.find())
			{
				String optionText = mOptions.group();
				
				
				Object o = data.get(nameOfInput);
				String valueToSet = o == null ? null : o.toString();
				
				if(isBlank(valueToSet) == false)
				{
					String declaredValue = getAttributeValue(optionText, "value");
					if(valueToSet.equals(declaredValue)) {
						optionText = addValueBeforeFirstElementTermination(optionText, "selected=\"selected\"");
					}
					else
					{
						optionText = StringUtils.replaceString(optionText, "selected=\"selected\"", "");
					}
				}
				sb.append(optionText);
			}
			
			sb.append(selectEnd);
			startingPoint = m.end();
		}
		
		if(startingPoint < text.length())
		{
			sb.append(text.substring(startingPoint));
		}
		
		return sb.toString();
	}

	
	public static String getAttributeValue(String source, String attributeName)
	{
		Pattern pValAttr = Pattern.compile("(?i)(\\s" + attributeName + "=[\"](.*?)[\"])", Pattern.CASE_INSENSITIVE);
		Matcher m = pValAttr.matcher(source);
		
		if(m.find()) {
			return m.group(2);
		}
		
		return null;
	}
	
	/**
	 * Adds the value to the first element padding the value with blank strings.
	 * @param source
	 * @param valueToAdd
	 * @return
	 */
	public static String addValueBeforeFirstElementTermination(String source, String valueToAdd)
	{
		int indexOfGT = source.indexOf(">");
		int indexOfSingleClose = source.indexOf("/>");
		
		int loc = Math.min(indexOfGT, indexOfSingleClose < 0 ? indexOfGT : indexOfSingleClose);
		
		return source.substring(0, loc) + " " + valueToAdd + " " + source.substring(loc);
		
	}
	
}
