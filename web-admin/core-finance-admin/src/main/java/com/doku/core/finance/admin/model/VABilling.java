package com.doku.core.finance.admin.model;

import java.math.BigDecimal;
import java.util.Date;

public class VABilling {
	private String username;
	private String name;
	private String referenceNo;
	private BigDecimal amount;
	private boolean expiry;
	private Date expired;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public boolean isExpiry() {
		return expiry;
	}

	public void setExpiry(boolean expiry) {
		this.expiry = expiry;
	}

}
