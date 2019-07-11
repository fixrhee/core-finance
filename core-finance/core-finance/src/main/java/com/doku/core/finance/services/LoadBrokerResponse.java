package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.Brokers;
import com.doku.core.finance.data.ResponseStatus;

public class LoadBrokerResponse {

	private List<Brokers> brokers;
	private ResponseStatus status;

	public List<Brokers> getBrokers() {
		return brokers;
	}

	public void setBrokers(List<Brokers> brokers) {
		this.brokers = brokers;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
