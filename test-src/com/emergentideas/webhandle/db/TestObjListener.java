package com.emergentideas.webhandle.db;

import com.emergentideas.webhandle.TestObj;

public class TestObjListener implements ObjectChangeListener<TestObj> {
	
	long startTime = System.currentTimeMillis();

	@Override
	public com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType[] listenFor() {
		return new ChangeType[] { ChangeType.POST_PERSIST, ChangeType.POST_UPDATE };
	}

	@Override
	public void changeEvent(com.emergentideas.webhandle.db.ObjectChangeListener.ChangeType type, TestObj t) {
		System.out.println(startTime + ": object changed");
	}

	
}
