package com.doku.core.finance.services;

import com.doku.core.finance.data.ResponseStatus;

public class ConfirmKYCResponse {

	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
