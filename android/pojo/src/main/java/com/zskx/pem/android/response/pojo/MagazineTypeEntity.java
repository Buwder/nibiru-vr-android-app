package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

public class MagazineTypeEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8265305992836527696L;
	/** 杂志类型Id */
	private String magazineTypeId;
	/** 杂志类型标题 */
	private String magazineTypeTitle;
	public String getMagazineTypeId() {
		return magazineTypeId;
	}
	public void setMagazineTypeId(String magazineTypeId) {
		this.magazineTypeId = magazineTypeId;
	}
	public String getMagazineTypeTitle() {
		return magazineTypeTitle;
	}
	public void setMagazineTypeTitle(String magazineTypeTitle) {
		this.magazineTypeTitle = magazineTypeTitle;
	}
	


	

}
