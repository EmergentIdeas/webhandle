package com.emergentideas.webhandle.eventbus;

public class Listener1 {

	protected boolean triggered = false;
	
	@EventListener("/{queue:.*}")
	public void listen(Number n, String queue) {
		triggered = true;
	}

	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
	
	
}
