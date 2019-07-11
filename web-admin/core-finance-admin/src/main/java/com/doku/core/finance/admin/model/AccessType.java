package com.doku.core.finance.admin.model;

public class AccessType {

	private Integer accessTypeID;
	private String name;
	private String description;
	private String internalName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

}
