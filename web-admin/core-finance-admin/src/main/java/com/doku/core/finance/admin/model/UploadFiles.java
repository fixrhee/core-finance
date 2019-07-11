package com.doku.core.finance.admin.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class UploadFiles implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

}
