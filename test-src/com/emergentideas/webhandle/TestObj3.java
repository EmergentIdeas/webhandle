package com.emergentideas.webhandle;

import java.util.List;

public class TestObj3 {

	public Integer sum(List<Integer> values) {
		int result = 0;
		for(Integer i : values) {
			result += i;
		}
		
		return result;
	}
}
