package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ParentMenu;
import com.doku.core.finance.data.ParentMenuData;
import com.doku.core.finance.data.ResponseStatus;

public class LoadParentMenuResponse {

	private List<ParentMenuData> parentMenu;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<ParentMenuData> getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(List<ParentMenuData> parentMenu) {
		this.parentMenu = parentMenu;
	}
}
