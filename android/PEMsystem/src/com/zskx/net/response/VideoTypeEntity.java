package com.zskx.net.response;

import java.io.Serializable;

public class VideoTypeEntity implements Serializable {

	/** 视频类型Id */
	private String videoTypeId;
	/** 视频类型标题 */
	private String videoTypeTitle;

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
