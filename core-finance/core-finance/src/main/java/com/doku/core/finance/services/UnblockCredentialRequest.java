package com.doku.core.finance.services;

public class UnblockCredentialRequest {

	private String username;
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

}
