package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.AccessType;
import com.doku.core.finance.data.ResponseStatus;

public class LoadAccessTypeResponse {

	private List<AccessType> accessType;
	private ResponseStatus status;

	public List<AccessType> getAccessType() {
		return accessType;
	}

	public void setAccessType(List<AccessType> accessType) {
		this.accessType = accessType;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
