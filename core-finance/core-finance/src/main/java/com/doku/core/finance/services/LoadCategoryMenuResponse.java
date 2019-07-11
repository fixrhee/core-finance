package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.CategoryMenuData;
import com.doku.core.finance.data.ResponseStatus;

public class LoadCategoryMenuResponse {

	private List<CategoryMenuData> categoryMenu;
	private ResponseStatus status;

	public List<CategoryMenuData> getCategoryMenu() {
		return categoryMenu;
	}

	public void setCategoryMenu(List<CategoryMenuData> categoryMenu) {
		this.categoryMenu = categoryMenu;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
