package com.grability.archies.db.model;

import java.util.Calendar;


public class Plate {
	


	private int systemId;
	private int subCategoryId;
	private String name;
	private String description;
	private String imgPath; 
	private String rightImgPath;  
	private String leftImgPath;  
	private String localImgPath; 
	private String localRightImgPath;  
	private String localLeftImgPath;  
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
	public int getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public String getRightImgPath() {
		return rightImgPath;
	}
	public void setRightImgPath(String rightImgPath) {
		this.rightImgPath = rightImgPath;
	}
	public String getLeftImgPath() {
		return leftImgPath;
	}
	public void setLeftImgPath(String leftImgPath) {
		this.leftImgPath = leftImgPath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocalImgPath() {
		return localImgPath;
	}
	public void setLocalImgPath(String localImgPath) {
		this.localImgPath = localImgPath;
	}
	public String getLocalRightImgPath() {
		return localRightImgPath;
	}
	public void setLocalRightImgPath(String localRightImgPath) {
		this.localRightImgPath = localRightImgPath;
	}
	public String getLocalLeftImgPath() {
		return localLeftImgPath;
	}
	public void setLocalLeftImgPath(String localLeftImgPath) {
		this.localLeftImgPath = localLeftImgPath;
	}
	

	
}
