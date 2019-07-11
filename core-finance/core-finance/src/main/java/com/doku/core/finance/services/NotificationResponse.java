package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.Notifications;
import com.doku.core.finance.data.ResponseStatus;

public class NotificationResponse {

	private List<Notifications> notification;
	private ResponseStatus status;

	public List<Notifications> getNotification() {
		return notification;
	}

	public void setNotification(List<Notifications> notification) {
		this.notification = notification;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
