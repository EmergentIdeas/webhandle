package com.emergentideas.webhandle.templates;

import org.apache.commons.jexl2.JexlEngine;

import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.Type;

@Type("com.emergentideas.webhandle.templates.ExpressionFactory")
@Name("jexlExpressionFactory")
public class JexlExpressionFactory implements ExpressionFactory {

	protected JexlEngine engine = new JexlEngine();
	
	public Expression createExpression(String expression) {
		return new EscapedExpression(engine, expression);
	}

}
