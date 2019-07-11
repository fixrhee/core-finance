package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.FeeByMember;
import com.doku.core.finance.data.ResponseStatus;

public class FeeByMemberResponse {

	private List<FeeByMember> feeByMember;
	private ResponseStatus status;

	public List<FeeByMember> getFeeByMember() {
		return feeByMember;
	}

	public void setFeeByMember(List<FeeByMember> feeByMember) {
		this.feeByMember = feeByMember;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
