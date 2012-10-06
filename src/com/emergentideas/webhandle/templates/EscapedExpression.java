package com.emergentideas.webhandle.templates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;

import com.emergentideas.utils.LocationJexlContext;
import com.emergentideas.utils.MapUtils;
import com.emergentideas.utils.StringUtils;
import com.emergentideas.webhandle.Location;

/**
 * Contains an expression which has had some of its variable names replaced so that the 
 * expression parser will use them.
 * @author kolz
 *
 */
public class EscapedExpression implements com.emergentideas.webhandle.templates.Expression {

	protected String expression;
	protected Map<String, String> replacements = Collections.synchronizedMap(new HashMap<String, String>());
	protected Expression jExpression;
	
	protected static Pattern variablePattern = Pattern.compile("[a-zA-Z]([a-zA-Z0-9\\-]*?/[a-zA-Z0-9]+)+?");
	protected static Pattern doubleQuoteLiteralPattern = Pattern.compile("([\"'])(.*?[^\\\\])\\1");
	protected static String variableReplacementPrefix = "locationVariable";
	protected static String literalReplacementPrefix = "literalValue";
	
	
	public EscapedExpression(JexlEngine engine, String expression) {
		this.expression = escape(expression);
		jExpression = engine.createExpression(this.expression);
	}
	
	protected EscapedExpression() {
		
	}
	
	public Object evaluate(Location location) {
		return jExpression.evaluate(new LocationJexlContext(location, replacements));
	}
	
	protected String escape(String sourceExpression) {
		int literalCount = 0;
		int variableCount = 0;
		
		Map<String, String> literalReplacements = new HashMap<String, String>();
		literalCount = replaceValuesByPattern(sourceExpression, doubleQuoteLiteralPattern, literalReplacements, literalCount, literalReplacementPrefix, 2);
		sourceExpression = StringUtils.replaceString(sourceExpression, literalReplacements);


		variableCount = replaceValuesByPattern(sourceExpression, variablePattern, replacements, variableCount, variableReplacementPrefix, 0);
		
		String changedExpression = StringUtils.replaceString(sourceExpression, replacements);
		
		// change the order of the replacements so that we look up by replace values
		replacements = MapUtils.reverse(replacements);
		
		// restore the literal variables
		changedExpression = StringUtils.replaceString(changedExpression, MapUtils.reverse(literalReplacements));
		
		return changedExpression;
	}
	
	protected int replaceValuesByPattern(String sourceExpression, Pattern literalPattern, Map<String,String> replacements, int count, String prefix, int group) {
		Matcher m = literalPattern.matcher(sourceExpression);
		while(m.find()) {
			String literalValue = m.group(group);
			if(replacements.containsKey(literalValue) == false) {
				replacements.put(literalValue, prefix + (count++));
			}
		}
		
		return count;
	}
	
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public Map<String, String> getReplacements() {
		return replacements;
	}
	
}
