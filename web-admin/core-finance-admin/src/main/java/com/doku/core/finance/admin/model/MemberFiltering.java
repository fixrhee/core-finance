package com.doku.core.finance.admin.model;

public class MemberFiltering {

	private Integer id;
	private Integer feeID;
	private String username;
	private boolean destination;

	public Integer getFeeID() {
		return feeID;
	}

	public void setFeeID(Integer feeID) {
		this.feeID = feeID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isDestination() {
		return destination;
	}

	public void setDestination(boolean destination) {
		this.destination = destination;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
