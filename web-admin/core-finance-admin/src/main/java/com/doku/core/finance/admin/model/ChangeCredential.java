package com.doku.core.finance.admin.model;

import java.io.Serializable;

public class ChangeCredential implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4436702336029138312L;
	private String username;
	private String msisdn;
	private String oldCredential;
	private String newCredential;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldCredential() {
		return oldCredential;
	}

	public void setOldCredential(String oldCredential) {
		this.oldCredential = oldCredential;
	}

	public String getNewCredential() {
		return newCredential;
	}

	public void setNewCredential(String newCredential) {
		this.newCredential = newCredential;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

}
