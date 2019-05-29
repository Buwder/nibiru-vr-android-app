package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class MagazineDetailEntity implements Serializable {

	/** 视频Id */
	private String magazineId;
	/** 视频类型 */
	private String magazineType;
	/** 视频图片 */
	private String magazineImage;
	/** 视频标题 */
	private String magazineTitle;
	/** 视频网络地址 */
	private String magazineUrl;

	public String getmagazineId() {
		return magazineId;
	}

	public void setmagazineId(String magazineId) {
		this.magazineId = magazineId;
	}

	public String getmagazineType() {
		return magazineType;
	}

	public void setmagazineType(String magazineType) {
		this.magazineType = magazineType;
	}

	public String getmagazineImage() {
		if (magazineImage.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(magazineImage)) {
			return magazineImage;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + magazineImage;
		}
	}

	public void setmagazineImage(String magazineImage) {
		this.magazineImage = magazineImage;
	}

	public String getmagazineTitle() {
		return magazineTitle;
	}

	public void setmagazineTitle(String magazineTitle) {
		this.magazineTitle = magazineTitle;
	}

	public String getmagazineUrl() {
		if (magazineUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(magazineUrl)) {
			return magazineUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + magazineUrl;
		}
	}

	public void setmagazineUrl(String magazineUrl) {
		this.magazineUrl = magazineUrl;
	}

}
