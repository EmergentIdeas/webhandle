package com.emergentideas.webhandle.bootstrap;

public class AtomAndObject {
	
	protected ConfigurationAtom atom;
	protected Object createdObject;
	
	public AtomAndObject() {}
	
	public AtomAndObject(ConfigurationAtom atom, Object createdObject) {
		super();
		this.atom = atom;
		this.createdObject = createdObject;
	}

	public ConfigurationAtom getAtom() {
		return atom;
	}
	public void setAtom(ConfigurationAtom atom) {
		this.atom = atom;
	}
	public Object getCreatedObject() {
		return createdObject;
	}
	public void setCreatedObject(Object createdObject) {
		this.createdObject = createdObject;
	}
	
	

}
