package com.emergentideas.webhandle.assumptions.oak;

public class StaticDataHolder {

	public static String data = "hello" + System.currentTimeMillis();

	public String nonStaticData;
	
	public StaticDataHolder() {
		nonStaticData = data;
	}
	
	public String toString() {
		return nonStaticData;
	}
}
