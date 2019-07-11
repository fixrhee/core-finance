package com.doku.core.finance.services;

import java.util.List;

import com.doku.core.finance.data.ResponseStatus;
import com.doku.core.finance.data.WebServices;

public class LoadWebserviceResponse {

	private List<WebServices> webservice;
	private Integer totalRecords;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<WebServices> getWebservice() {
		return webservice;
	}

	public void setWebservice(List<WebServices> webservice) {
		this.webservice = webservice;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}
