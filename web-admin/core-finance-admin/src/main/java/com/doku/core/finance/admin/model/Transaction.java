package com.doku.core.finance.admin.model;

import java.io.Serializable;

public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromMember;
	private String toMember;
	private Integer amount;
	private String description;
	private String credential;
	private String otp;
	private Integer transferTypeID;
	private String transactionType;

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

}
