package com.doku.core.finance.services;

import com.doku.core.finance.data.ResponseStatus;

public class GeneratePaymentTicketResponse {

	private String ticket;
	private ResponseStatus status;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
