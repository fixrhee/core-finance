package com.doku.core.finance.services;

import java.util.List;
import com.doku.core.finance.data.Groups;
import com.doku.core.finance.data.ResponseStatus;

public class LoadGroupsResponse {

	private List<Groups> groupsList;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<Groups> getGroupsList() {
		return groupsList;
	}

	public void setGroupsList(List<Groups> groupsList) {
		this.groupsList = groupsList;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}
