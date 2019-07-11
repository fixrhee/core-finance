package com.doku.core.finance.services;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.TransferTypes;

public class LoadTransferTypesByIDResponse {
	
	private TransferTypes transferTypes;
	private ResponseStatus status;

	public TransferTypes getTransferTypes() {
		return transferTypes;
	}

	public void setTransferTypes(TransferTypes transferTypes) {
		this.transferTypes = transferTypes;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
