package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.TransferNotifications;

public class TransferTypeNotificationResponse {

	private List<TransferNotifications> notification;
	private ResponseStatus status;

	public List<TransferNotifications> getNotification() {
		return notification;
	}

	public void setNotification(List<TransferNotifications> notification) {
		this.notification = notification;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
