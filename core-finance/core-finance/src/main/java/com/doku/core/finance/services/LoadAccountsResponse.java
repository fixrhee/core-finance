package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.Accounts;
import com.doku.core.finance.data.ResponseStatus;

public class LoadAccountsResponse {

	private List<Accounts> account;
	private ResponseStatus status;
	private Integer totalRecords;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<Accounts> getAccount() {
		return account;
	}

	public void setAccount(List<Accounts> account) {
		this.account = account;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}
