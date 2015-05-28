package com.grability.archies.db.model;

import java.util.Calendar;


public class Category {
	


	private int systemId;
	private String name;
	private String imgPath;
	private String localImgPath;
	private boolean enabled;
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
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
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
	public String getLocalImgPath() {
		return localImgPath;
	}
	public void setLocalImgPath(String localImgPath) {
		this.localImgPath = localImgPath;
	}
	

	
}
