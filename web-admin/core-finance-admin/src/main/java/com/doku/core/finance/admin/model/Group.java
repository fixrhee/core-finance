package com.doku.core.finance.admin.model;

public class Group {
	private String name;
	private String description;
	private Integer pinLength;
	private Integer maxPinAttempt;
	private String notification;

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

	public Integer getPinLength() {
		return pinLength;
	}

	public void setPinLength(Integer pinLength) {
		this.pinLength = pinLength;
	}

	public Integer getMaxPinAttempt() {
		return maxPinAttempt;
	}

	public void setMaxPinAttempt(Integer maxPinAttempt) {
		this.maxPinAttempt = maxPinAttempt;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

}
