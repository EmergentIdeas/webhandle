package com.emergentideas.webhandle;

import java.util.Date;

public class TestObj4<E, T> {
	
	protected Date d;
	private Number e;
	protected Long f;
	protected long g;
	

	public String plus2(T num) {
		if(num instanceof Integer) {
			num = (T)(Object) new Integer( ((Integer)num).intValue() + 2);
		}
		return num.toString();
	}
}
