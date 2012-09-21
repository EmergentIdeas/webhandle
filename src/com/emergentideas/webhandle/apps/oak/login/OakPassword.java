package com.emergentideas.webhandle.apps.oak.login;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OakPassword {
	
	@Id
	protected String userId;
	
	protected byte[] hashedPassword;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public byte[] getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	

}
