package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.TransferTypes;

public class LoadTransferTypesResponse {

	private List<TransferTypes> transferType;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<TransferTypes> getTransferType() {
		return transferType;
	}

	public void setTransferType(List<TransferTypes> transferType) {
		this.transferType = transferType;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}
