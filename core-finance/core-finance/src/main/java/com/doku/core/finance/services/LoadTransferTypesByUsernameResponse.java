package com.doku.core.finance.services;

import java.util.List;
import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.TransferTypes;

public class LoadTransferTypesByUsernameResponse {

	private List<TransferTypes> transferTypes;
	private ResponseStatus status;

	public List<TransferTypes> getTransferTypes() {
		return transferTypes;
	}

	public void setTransferTypes(List<TransferTypes> transferTypes) {
		this.transferTypes = transferTypes;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
