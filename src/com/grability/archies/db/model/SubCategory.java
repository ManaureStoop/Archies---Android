package com.grability.archies.db.model;

import java.util.Calendar;


public class SubCategory {
	
	
	private int systemId;
	private int categoryId;
	private String name;
	private boolean enabled;
	private boolean additionEnabled;
	private Calendar updatedAt;
	

	public Calendar getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getSystemId() {
		return systemId;
	}
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
	public boolean isAdditionEnabled() {
		return additionEnabled;
	}
	public void setAdditionEnabled(boolean additionEnabled) {
		this.additionEnabled = additionEnabled;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
