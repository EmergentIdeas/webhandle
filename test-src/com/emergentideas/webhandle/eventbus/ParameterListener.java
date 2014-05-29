package com.emergentideas.webhandle.eventbus;

public class ParameterListener {
	
	protected Object event;
	protected String one;
	protected Integer two;
	
	@EventListener("/{one}/{two}")
	public void listen(Object event, String one, Integer two) {
		this.event = event;
		this.one = one;
		this.two = two;
	}

	public Object getEvent() {
		return event;
	}

	public void setEvent(Object event) {
		this.event = event;
	}

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public Integer getTwo() {
		return two;
	}

	public void setTwo(Integer two) {
		this.two = two;
	}
	
	

}
