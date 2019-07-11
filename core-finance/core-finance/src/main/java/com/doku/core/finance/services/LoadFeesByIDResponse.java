package com.doku.core.finance.services;

import com.doku.core.finance.data.Fees;
import com.doku.core.finance.data.ResponseStatus;

public class LoadFeesByIDResponse {

	private Fees fee;
	private ResponseStatus status;

	public Fees getFee() {
		return fee;
	}

	public void setFee(Fees fee) {
		this.fee = fee;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
