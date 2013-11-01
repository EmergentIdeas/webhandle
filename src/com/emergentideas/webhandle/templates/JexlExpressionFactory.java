package com.emergentideas.webhandle.templates;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlEngine;

import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.Type;

@Type("com.emergentideas.webhandle.templates.ExpressionFactory")
@Name("jexlExpressionFactory")
public class JexlExpressionFactory implements ExpressionFactory {

	protected JexlEngine engine = new JexlEngine();
	
	protected ThreadLocal<Map<String, EscapedExpression>> expressionCache = new ThreadLocal<Map<String,EscapedExpression>>();
	
	public Expression createExpression(String expression) {
		Map<String, EscapedExpression> cache = getCache();
		if(cache.containsKey(expression)) {
			return cache.get(expression);
		}
		
		EscapedExpression ee = new EscapedExpression(engine, expression);
		cache.put(expression, ee);
		return ee;
	}
	
	protected Map<String, EscapedExpression> getCache() {
		Map<String, EscapedExpression> cache = expressionCache.get();
		if(cache == null) {
			cache = new HashMap<String, EscapedExpression>();
			expressionCache.set(cache);
		}
		return cache;
	}

}
