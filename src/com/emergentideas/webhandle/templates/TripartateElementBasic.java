package com.emergentideas.webhandle.templates;

public class TripartateElementBasic implements TripartateElement {

	public String conditionalExpression;
	public String dataSelectorExpression;
	public String handlingExpression;
	
	public TripartateElementBasic() {
		
	}
	
	public TripartateElementBasic(String conditionalExpression, String dataSelectorExpression, String handlingExpression) {
		this.conditionalExpression = conditionalExpression;
		this.dataSelectorExpression = dataSelectorExpression;
		this.handlingExpression = handlingExpression;
	}
	
	public String getConditionalExpression() {
		return conditionalExpression;
	}
	public void setConditionalExpression(String conditionalExpression) {
		this.conditionalExpression = conditionalExpression;
	}
	public String getDataSelectorExpression() {
		return dataSelectorExpression;
	}
	public void setDataSelectorExpression(String dataSelectorExpression) {
		this.dataSelectorExpression = dataSelectorExpression;
	}
	public String getHandlingExpression() {
		return handlingExpression;
	}
	public void setHandlingExpression(String handlingExpression) {
		this.handlingExpression = handlingExpression;
	}
	
	
}
