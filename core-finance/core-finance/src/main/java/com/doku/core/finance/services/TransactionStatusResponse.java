package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.TransferHistory;

public class TransactionStatusResponse {

	private List<TransferHistory> transfers;
	private ResponseStatus status;

	public List<TransferHistory> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<TransferHistory> transfers) {
		this.transfers = transfers;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
