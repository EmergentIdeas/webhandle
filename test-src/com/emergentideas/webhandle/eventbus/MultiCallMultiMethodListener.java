package com.emergentideas.webhandle.eventbus;

public class MultiCallMultiMethodListener {

	int count = 0;
	
	@EventListener({"/one", "/{path}"})
	public void listen() {
		count++;
	}

	@EventListener({"/one", "/{path}"})
	public void listen2() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
