package com.doku.core.finance.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.doku.core.finance.data.PaymentFields;

public class PaymentRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -63039604936619552L;
	private String fromMember;
	private String toMember;
	private Integer transferTypeID;
	private Integer accessTypeID;
	private String credential;
	private BigDecimal amount;
	private String traceNumber;
	private String referenceNumber;
	private String description;
	private String originator;
	private RequestEviction requestEviction;
	private List<PaymentFields> paymentFields;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PaymentFields> getPaymentFields() {
		return paymentFields;
	}

	public void setPaymentFields(List<PaymentFields> paymentFields) {
		this.paymentFields = paymentFields;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public RequestEviction getRequestEviction() {
		return requestEviction;
	}

	public void setRequestEviction(RequestEviction requestEviction) {
		this.requestEviction = requestEviction;
	}

}
