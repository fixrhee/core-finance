package com.doku.core.finance.services;

import com.doku.core.finance.data.ResponseStatus;

public class AuthResponse {

	private String token;
	private ResponseStatus status;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
