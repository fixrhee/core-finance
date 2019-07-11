package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.AccountPermissions;
import com.doku.core.finance.data.ResponseStatus;

public class LoadAccountPermissionsResponse {

	private List<AccountPermissions> accountPermission;
	private ResponseStatus status;


	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<AccountPermissions> getAccountPermission() {
		return accountPermission;
	}

	public void setAccountPermission(List<AccountPermissions> accountPermission) {
		this.accountPermission = accountPermission;
	}

}
