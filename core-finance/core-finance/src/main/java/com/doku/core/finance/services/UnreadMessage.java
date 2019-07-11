package com.doku.core.finance.services;

import com.doku.core.finance.data.ResponseStatus;

public class UnreadMessage {

	private Integer unread;
	private ResponseStatus status;

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
