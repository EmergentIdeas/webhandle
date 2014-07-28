package com.emergentideas.webhandle.transformers;

import com.emergentideas.webhandle.NoInject;

public class BooleanSettersObj {
	
	protected boolean member1 = true;
	protected boolean member2 = true;
	protected Boolean member3 = true;
	protected boolean member4 = true;
	
	public void turnAllValues(boolean val) {
		member1 = val;
		member2 = val;
		member3 = val;
		member4 = val;
	}
	
	public boolean isMember1() {
		return member1;
	}
	
	public void setMember1(boolean member1) {
		this.member1 = member1;
	}
	
	public boolean isMember2() {
		return member2;
	}
	
	@NoInject
	public void setMember2(boolean member2) {
		this.member2 = member2;
	}
	
	public Boolean getMember3() {
		return member3;
	}
	
	public void setMember3(Boolean member3) {
		this.member3 = member3;
	}
	
	public boolean isMember4() {
		return member4;
	}
	
	public void fluggleMember4(boolean member4) {
		this.member4 = member4;
	}
	
	

}
