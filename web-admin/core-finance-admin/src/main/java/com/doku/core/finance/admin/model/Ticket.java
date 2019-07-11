package com.doku.core.finance.admin.model;

import java.io.Serializable;

public class Ticket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ticketID;
	private String credential;

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

}
