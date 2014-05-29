package com.emergentideas.webhandle.eventbus;

public class MultiCallListener {

	int count = 0;
	
	@EventListener({"/one", "/{path}"})
	public void listen() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
