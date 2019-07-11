package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.NotificationMessage;
import com.doku.core.finance.data.ResponseStatus;

public class LoadMessageResponse {

	private List<NotificationMessage> message;
	private ResponseStatus status;

	public List<NotificationMessage> getMessage() {
		return message;
	}

	public void setMessage(List<NotificationMessage> message) {
		this.message = message;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
