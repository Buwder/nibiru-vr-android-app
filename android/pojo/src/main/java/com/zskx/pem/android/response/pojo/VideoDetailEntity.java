package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

/**
 * @author solidsnake
 *
 */
public class VideoDetailEntity implements Serializable {

	/** 视频Id */
	private String videoId;
	/** 视频类型 */
	private String videoType;
	/** 视频图片 */
	private String videoImage;
	/** 视频标题 */
	private String videoTitle;
	/** 视频网络地址 */
	private String videoUrl;
	
	/**
	 *	时评概述 
	 */
	private String videoSummary;

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
		return videoImage;
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
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoSummary() {
		return videoSummary;
	}

	public void setVideoSummary(String videoSummary) {
		this.videoSummary = videoSummary;
	}
	
	
}
