package com.emergentideas.webhandle.assumptions.oak;

import com.emergentideas.utils.ReflectionUtils;

public class LoadedRunnable implements Runnable {
	
	public LoadedRunnable() {
		
	}

	@Override
	public void run() {
		try {
			StaticDataHolder sdh = new StaticDataHolder();
			System.out.println(sdh.data);
			System.out.println(ReflectionUtils.getClassForName(StaticDataHolder.class.getName()).newInstance().toString());
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
