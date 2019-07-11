package com.doku.core.finance.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransferHistory {

	private Integer id;
	private String traceNumber;
	private MemberView fromMember;
	private MemberView toMember;
	private TransferTypeFields transferType;

	public MemberView getFromMember() {
		return fromMember;
	}

	public void setFromMember(MemberView fromMember) {
		this.fromMember = fromMember;
	}

	public MemberView getToMember() {
		return toMember;
	}

	public void setToMember(MemberView toMember) {
		this.toMember = toMember;
	}

	public List<PaymentFields> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<PaymentFields> customFields) {
		this.customFields = customFields;
	}

	private BigDecimal amount;
	private String formattedAmount;
	private String transactionNumber;
	private String parentID;
	private String description;
	private boolean chargedBack;
	private String transactionState;
	private Date transactionDate;
	private List<PaymentFields> customFields;

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isChargedBack() {
		return chargedBack;
	}

	public void setChargedBack(boolean chargedBack) {
		this.chargedBack = chargedBack;
	}

	public String getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}

	public TransferTypeFields getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypeFields transferType) {
		this.transferType = transferType;
	}

	public String getFormattedAmount() {
		return formattedAmount;
	}

	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}

}
