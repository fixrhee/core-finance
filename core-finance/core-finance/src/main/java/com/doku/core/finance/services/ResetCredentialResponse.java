package com.doku.core.finance.services;

import com.doku.core.finance.data.Members;

public class ResetCredentialResponse {

	private String credential;
	private Members member;

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public Members getMember() {
		return member;
	}

	public void setMember(Members member) {
		this.member = member;
	}
}
