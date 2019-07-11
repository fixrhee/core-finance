package com.doku.core.finance.services;

import com.doku.core.finance.data.Accounts;
import com.doku.core.finance.data.ResponseStatus;

public class LoadAccountsByIDResponse {

	private Accounts account;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

}
