package com.emergentideas.webhandle.eventbus;

public class NoEventListener {

	protected boolean triggered = false;
	
	@EventListener("{path:.*}")
	public void listen() {
		triggered = true;
	}

	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
	
	
}
