package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

public class VideoTypeEntity implements Serializable {

	/** 视频类型Id */
	private String videoTypeId;
	/** 视频类型标题 */
	private String videoTypeTitle;
	/** 视频类型图标 **/
	private String videoTypeImageUrl;
	/*** 视频类型概述 **/
	private String videoTypeSummary;

	public String getVideoTypeId() {
		return videoTypeId;
	}

	public void setVideoTypeId(String videoTypeId) {
		this.videoTypeId = videoTypeId;
	}

	public String getVideoTypeTitle() {
		return videoTypeTitle;
	}

	public void setVideoTypeTitle(String videoTypeTitle) {
		this.videoTypeTitle = videoTypeTitle;
	}

	public String getVideoTypeImageUrl() {
		return videoTypeImageUrl;
	}

	public void setVideoTypeImageUrl(String videoTypeImageUrl) {
		this.videoTypeImageUrl = videoTypeImageUrl;
	}

	public String getVideoTypeSummary() {
		return videoTypeSummary;
	}

	public void setVideoTypeSummary(String videoTypeSummary) {
		this.videoTypeSummary = videoTypeSummary;
	}
	
	

}
