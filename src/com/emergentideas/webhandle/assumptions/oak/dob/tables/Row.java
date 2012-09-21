package com.emergentideas.webhandle.assumptions.oak.dob.tables;

import java.util.ArrayList;
import java.util.List;

public class Row {
	protected List<Object> values;

	public Row() {
		values = new ArrayList<Object>();
	}
	
	public Row(List<Object> values) {
		this.values = values;
	}
	
	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
