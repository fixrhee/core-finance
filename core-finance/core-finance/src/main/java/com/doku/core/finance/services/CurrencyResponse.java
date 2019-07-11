package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.Currencies;
import com.doku.core.finance.data.ResponseStatus;

public class CurrencyResponse {

	private List<Currencies> currency;
	private ResponseStatus status;

	public List<Currencies> getCurrency() {
		return currency;
	}

	public void setCurrency(List<Currencies> currency) {
		this.currency = currency;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
