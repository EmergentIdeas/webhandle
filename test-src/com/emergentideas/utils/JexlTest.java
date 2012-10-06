package com.emergentideas.utils;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.junit.Test;

import com.emergentideas.webhandle.AppLocation;
import com.emergentideas.webhandle.Location;

public class JexlTest {

	@Test
	public void testLocationBasedExpressions() throws Exception {
		JexlEngine jexl = new JexlEngine();
		jexl.setSilent(false);
		Map functions = new HashMap();
		functions.put(null, ForFunctions.class);
		jexl.setFunctions(functions);
		
		Expression e = jexl.createExpression( "'hello' =~ @l" );

		Location location = new AppLocation();
		Map<String,String> vals = new HashMap<String, String>();
		vals.put("else", "here");
		location.put("something", vals);
		location.put("l", Arrays.asList(new String[] { "hello", "one" }));
		location.put("m", "mystring");
		
	    // populate the context
	    JexlContext context = new LocationJexlContext(location);
	    Object result = e.evaluate(context);
	    System.out.println(result);
	    
	    e = jexl.createExpression( "newval = 1" );
	    e.evaluate(context);
	    System.out.println(location.get("newval"));
	    
	    e = jexl.createExpression( "m.substring(newval)" );
	    result = e.evaluate(context);
	    System.out.println(result);

	    e = jexl.createExpression( "l" );
	    result = e.evaluate(context);
	    System.out.println(result);
	    
	    e = jexl.createExpression( "if(newval == 1) { 'one' } else { 'other'}" );
	    result = e.evaluate(context);
	    System.out.println(result);

	    e = jexl.createExpression( "something['else']" );
	    result = e.evaluate(context);
	    System.out.println(result);

		e = jexl.createExpression( "'hello' =~ [$l]" );
	    result = e.evaluate(context);
	    System.out.println(result);
	    
		e = jexl.createExpression( "$l" );
	    result = e.evaluate(context);
	    System.out.println(result);

	    e = jexl.createExpression( "@l.1" );
	    result = e.evaluate(context);
	    System.out.println(result);
	    
	    e = jexl.createExpression( "addA('hello')" );
	    result = e.evaluate(context);
	    System.out.println(result);

	    e = jexl.createExpression( "addA('hi')" );
	    result = e.evaluate(context);
	    System.out.println(result);

	}
}
