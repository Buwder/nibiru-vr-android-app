package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class VideoTypeEntity implements Serializable {

	/** 视频类型Id */
	private String videoTypeId;
	/** 视频类型图标 **/
	private String videoTypeImageUrl;
	/*** 视频类型概述 **/
	private String videoTypeSummary;

	/** 视频类型标题 */
	private String videoTypeTitle;

	public String getVideoTypeImageUrl() {
		if (videoTypeImageUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(videoTypeImageUrl)) {
			return videoTypeImageUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + videoTypeImageUrl;
		}
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

}
