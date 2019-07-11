package com.doku.core.finance.admin.model;

public class MemberCredential {

	private String username;
	private String accessType;
	private String credential;
	private String confirmCredential;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getConfirmCredential() {
		return confirmCredential;
	}

	public void setConfirmCredential(String confirmCredential) {
		this.confirmCredential = confirmCredential;
	}

}
