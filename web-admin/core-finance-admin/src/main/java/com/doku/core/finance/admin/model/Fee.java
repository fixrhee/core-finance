package com.doku.core.finance.admin.model;

import java.math.BigDecimal;

public class Fee {

	private Integer id;
	private Integer transferTypeID;
	private String name;
	private String description;

	private Integer fromMemberOpt;
	private String fromMember;
	private Integer fromAccountOpt;
	private String fromAccountName;

	private Integer toMemberOpt;
	private String toMember;
	private Integer toAccountOpt;
	private String toAccountName;

	private BigDecimal minAmount;
	private BigDecimal maxAmount;

	private boolean periode;
	private String startDate;
	private String endDate;

	private boolean deductAmount;
	private BigDecimal fixedAmount;
	private double percentage;

	private boolean allowFromAllMember;
	private boolean allowToAllMember;

	private boolean priority;
	private boolean enabled;

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

	public Integer getFromMemberOpt() {
		return fromMemberOpt;
	}

	public void setFromMemberOpt(Integer fromMemberOpt) {
		this.fromMemberOpt = fromMemberOpt;
	}

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

	public Integer getFromAccountOpt() {
		return fromAccountOpt;
	}

	public void setFromAccountOpt(Integer fromAccountOpt) {
		this.fromAccountOpt = fromAccountOpt;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}

	public Integer getToMemberOpt() {
		return toMemberOpt;
	}

	public void setToMemberOpt(Integer toMemberOpt) {
		this.toMemberOpt = toMemberOpt;
	}

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public Integer getToAccountOpt() {
		return toAccountOpt;
	}

	public void setToAccountOpt(Integer toAccountOpt) {
		this.toAccountOpt = toAccountOpt;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
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

	public boolean isPeriode() {
		return periode;
	}

	public void setPeriode(boolean periode) {
		this.periode = periode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(boolean deductAmount) {
		this.deductAmount = deductAmount;
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

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public boolean isAllowFromAllMember() {
		return allowFromAllMember;
	}

	public void setAllowFromAllMember(boolean allowFromAllMember) {
		this.allowFromAllMember = allowFromAllMember;
	}

	public boolean isAllowToAllMember() {
		return allowToAllMember;
	}

	public void setAllowToAllMember(boolean allowToAllMember) {
		this.allowToAllMember = allowToAllMember;
	}

}
