package com.doku.core.finance.admin.model;

import java.io.Serializable;

public class BankAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7099678332409287417L;
	private String bankTransfer;
	private String accountName;
	private String accountNumber;
	private String description;

	public String getBankTransfer() {
		return bankTransfer;
	}

	public void setBankTransfer(String bankTransfer) {
		this.bankTransfer = bankTransfer;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
