package com.doku.core.finance.admin.model;

import java.math.BigDecimal;

public class Broker {

	private Integer id;
	private Integer feeID;
	private String name;
	private String description;
	private String toMember;
	private String toAccountName;
	private BigDecimal fixedAmount;
	private double percentage;
	private boolean enabled;
	private boolean deductAllFee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFeeID() {
		return feeID;
	}

	public void setFeeID(Integer feeID) {
		this.feeID = feeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public BigDecimal getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(BigDecimal fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDeductAllFee() {
		return deductAllFee;
	}

	public void setDeductAllFee(boolean deductAllFee) {
		this.deductAllFee = deductAllFee;
	}

}
