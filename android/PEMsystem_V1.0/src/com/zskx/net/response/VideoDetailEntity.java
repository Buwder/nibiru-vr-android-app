package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class VideoDetailEntity implements Serializable {

	/** 视频Id */
	private String videoId;
	/** 视频类型 */
	private String videoType;
	/** 视频图片 */
	private String videoImage;
	/*** 视频概述 */
	private String videoSummary;

	/** 视频标题 */
	private String videoTitle;
	/** 视频网络地址 */
	private String videoUrl;

	public String getVideoSummary() {
		return videoSummary;
	}

	public void setVideoSummary(String videoSummary) {
		this.videoSummary = videoSummary;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getVideoImage() {
		if (videoImage.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(videoImage)) {
			return videoImage;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + videoImage;
		}
	}

	public void setVideoImage(String videoImage) {
		this.videoImage = videoImage;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getVideoUrl() {
		if (videoUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(videoUrl)) {
			return videoUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + videoUrl;
		}
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

}
