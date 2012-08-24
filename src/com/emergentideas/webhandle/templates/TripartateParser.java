package com.emergentideas.webhandle.templates;

import java.util.ArrayList;
import java.util.List;

public class TripartateParser {
	
	protected String initiatingSequence = "__";
	protected String conditionSeparator = "??";
	protected String handlingSeparator = "::";
	protected String terminatingSequence = "__";
	
	public List<Element> parse(String data) {
		List<Element> result = new ArrayList<Element>();
		
		StringBuilder sb = new StringBuilder(data);
		
		while(sb.length() > 0) {
			int nextInitiator = sb.indexOf(initiatingSequence);
			if(nextInitiator < 0) {
				result.add(new StringElementBasic(sb.toString()));
				break;
			}
			else {
				// Eat the available string
				if(nextInitiator > 0) {
					// checking to make sure it's not a zero length string
					result.add(new StringElementBasic(sb.substring(0, nextInitiator)));
					sb.delete(0, nextInitiator);
				}
				
				// delete the initiator
				sb.delete(0, initiatingSequence.length());
				
				int nextTerminator = sb.indexOf(terminatingSequence);
				String tripartateExpression = sb.substring(0, nextTerminator);
				sb.delete(0, nextTerminator);
				result.add(parseTripartateExpression(tripartateExpression));
				
				// delete the terminator
				sb.delete(0, terminatingSequence.length());
			}
			
		}
		
		
		return result;
	}
	
	protected TripartateElement parseTripartateExpression(String expression) {
		TripartateElementBasic tp = new TripartateElementBasic();
		
		int conditionalIndex = expression.indexOf(conditionSeparator);
		if(conditionalIndex > 0) {
			tp.setConditionalExpression(expression.substring(0, conditionalIndex));
		}
		if(conditionalIndex >=0) {
			expression = expression.substring(conditionalIndex + conditionSeparator.length());
		}
		
		int handlingIndex = expression.indexOf(handlingSeparator);
		if(handlingIndex > 0) {
			tp.setDataSelectorExpression(expression.substring(0, handlingIndex));
		}
		else if(handlingIndex < 0) {
			tp.setDataSelectorExpression(expression);
			expression = "";
		}
		
		if(handlingIndex >= 0) {
			expression = expression.substring(handlingIndex + handlingSeparator.length());
		}
		
		if(expression.length() > 0) {
			tp.setHandlingExpression(expression);
		}
		
		return tp;
	}

}
