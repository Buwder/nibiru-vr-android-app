package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class MusicDetailEntity implements Serializable {

	/** 音乐id */
	private String musicId;
	/** 音乐类型 */
	private String musicType;
	/** 音乐图片 */
	private String musicImage;
	/** 音乐标题 */
	private String musicTitle;
	/** 音乐类型描述 */
	private String musicTypeDescription;
	/** 音乐网络位置 */
	private String musicUrl;

	public String getMusicId() {
		return musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public String getMusicType() {
		return musicType;
	}

	public void setMusicType(String musicType) {
		this.musicType = musicType;
	}

	public String getMusicImage() {
		System.out.println("musicImage:"+musicImage);
			if (musicImage.startsWith(NetConfiguration.IMAGE_DOWNLOAD_ADD)
					|| "".equals(musicImage)) {
				return musicImage;
			} else {
				return NetConfiguration.IMAGE_DOWNLOAD_ADD + musicImage;
			}
	}

	public void setMusicImage(String musicImage) {
		this.musicImage = musicImage;
	}

	public String getMusicTitle() {
		return musicTitle;
	}

	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}

	public String getMusicTypeDescription() {
		return musicTypeDescription;
	}

	public void setMusicTypeDescription(String musicTypeDescription) {
		this.musicTypeDescription = musicTypeDescription;
	}

	public String getMusicUrl() {
		if (musicUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(musicUrl)) {
			return musicUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + musicUrl;
		}

	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

}
