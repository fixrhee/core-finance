package com.doku.core.finance.admin.model;

import java.io.Serializable;

public class ReversePayment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3127805070788353490L;
	String tracenumber;
	String toFromMember;
	String amount;
	String description;
	String remark;
	String credential;
	String transactionNumber;
	String transactionDate;

	public String getTracenumber() {
		return tracenumber;
	}

	public void setTracenumber(String tracenumber) {
		this.tracenumber = tracenumber;
	}

	public String getToFromMember() {
		return toFromMember;
	}

	public void setToFromMember(String toFromMember) {
		this.toFromMember = toFromMember;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
