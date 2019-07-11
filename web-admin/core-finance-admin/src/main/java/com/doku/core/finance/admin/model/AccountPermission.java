package com.doku.core.finance.admin.model;

import java.math.BigDecimal;

public class AccountPermission {

	private String group;
	private Integer id;
	private Integer accountID;
	private BigDecimal creditLimit;
	private BigDecimal upperCreditLimit;
	private BigDecimal lowerCreditLimit;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getUpperCreditLimit() {
		return upperCreditLimit;
	}

	public void setUpperCreditLimit(BigDecimal upperCreditLimit) {
		this.upperCreditLimit = upperCreditLimit;
	}

	public BigDecimal getLowerCreditLimit() {
		return lowerCreditLimit;
	}

	public void setLowerCreditLimit(BigDecimal lowerCreditLimit) {
		this.lowerCreditLimit = lowerCreditLimit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
