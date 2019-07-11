package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.WelcomeMenu;

public class LoadWelcomeMenuResponse {

	private List<WelcomeMenu> welcomeLink;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<WelcomeMenu> getWelcomeLink() {
		return welcomeLink;
	}

	public void setWelcomeLink(List<WelcomeMenu> welcomeLink) {
		this.welcomeLink = welcomeLink;
	}

}
