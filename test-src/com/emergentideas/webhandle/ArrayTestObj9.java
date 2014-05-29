package com.emergentideas.webhandle;

import java.util.List;

public class ArrayTestObj9 {
	
	protected Object assigned;
	
	public void intValue(int value) {
		assigned = value;
	}
	
	public void integer(Integer value) {
		assigned = value;
	}

	
	public void ints(int[] value) {
		assigned = value;
	}
	
	public void integers(Integer[] value) {
		assigned = value;
	}
	
	public void string(String value) {
		assigned = value;
	}
	
	public void strings(String[] value) {
		assigned = value;
	}
	
	public void integersList(List<Integer> value) {
		assigned = value;
	}

	public Object getAssigned() {
		return assigned;
	}
	
	public void clear() {
		assigned = null;
	}
}
