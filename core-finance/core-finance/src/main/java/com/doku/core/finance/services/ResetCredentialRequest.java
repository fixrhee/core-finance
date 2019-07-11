package com.doku.core.finance.services;

public class ResetCredentialRequest {

	private String username;
	private String email;
	private Integer accessTypeID;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
