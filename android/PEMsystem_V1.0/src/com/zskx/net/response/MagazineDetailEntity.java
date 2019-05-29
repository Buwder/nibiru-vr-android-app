package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class MagazineDetailEntity implements Serializable {

	/** 杂志Id */
	private String magazineId;
	/** 杂志图片网络地址 */
	private String magazineImageUrl;
	/** 杂志标题 */
	private String magazineTitle;
	/** 杂志简介 */
	private String magazineSummary;
	/**
	 * 移动设备杂志信息源（内容）
	 */
	private String mobileMagazineInformations;

	public String getMagazineId() {
		return magazineId;
	}

	public void setMagazineId(String magazineId) {
		this.magazineId = magazineId;
	}

	public String getMagazineImageUrl() {
		if (magazineImageUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(magazineImageUrl)) {
			return magazineImageUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + magazineImageUrl;
		}
	}

	public void setMagazineImageUrl(String magazineImageUrl) {
		this.magazineImageUrl = magazineImageUrl;
	}

	public String getMagazineTitle() {
		return magazineTitle;
	}

	public void setMagazineTitle(String magazineTitle) {
		this.magazineTitle = magazineTitle;
	}

	public String getMagazineSummary() {
		return magazineSummary;
	}

	public void setMagazineSummary(String magazineSummary) {
		this.magazineSummary = magazineSummary;
	}

	public String getMobileMagazineInformations() {
		return mobileMagazineInformations;
	}

	public void setMobileMagazineInformations(String mobileMagazineInformations) {
		this.mobileMagazineInformations = mobileMagazineInformations;
	}

	public void setmagazineTitle(String magazineTitle) {
		this.magazineTitle = magazineTitle;
	}

}
