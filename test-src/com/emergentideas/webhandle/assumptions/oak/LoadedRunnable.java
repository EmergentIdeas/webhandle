package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.utils.ReflectionUtils;

import static org.junit.Assert.*;

public class LoadedRunnable implements Runnable {
	
	protected StaticDataHolder sdh;
	public LoadedRunnable() {
		
	}

	@Override
	public void run() {
		try {
			StaticDataHolder sdh = new StaticDataHolder();
			String dynoDate = ReflectionUtils.getClassForName(StaticDataHolder.class.getName()).newInstance().toString();
			assertEquals(sdh.data, dynoDate);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String toString() {
		return sdh.data;
	}
	
}
