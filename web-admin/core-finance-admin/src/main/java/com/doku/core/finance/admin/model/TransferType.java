package com.doku.core.finance.admin.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5161686647448281478L;
	private Integer id;
	private String fromAccountName;
	private String toAccountName;
	private String name;
	private String description;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;
	private BigDecimal otpThreshold;
	private Integer maxCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getOtpThreshold() {
		return otpThreshold;
	}

	public void setOtpThreshold(BigDecimal otpThreshold) {
		this.otpThreshold = otpThreshold;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

}
