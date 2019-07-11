package com.doku.core.finance.services;

import com.doku.core.finance.data.Groups;
import com.doku.core.finance.data.ResponseStatus;

public class LoadGroupsByIDResponse {

	private Groups groups;
	private ResponseStatus status;

	public LoadGroupsByIDResponse() {
	}

	public Groups getGroups() {
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
