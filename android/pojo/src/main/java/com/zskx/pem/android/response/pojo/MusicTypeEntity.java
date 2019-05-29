package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

public class MusicTypeEntity implements Serializable {

	/** 音乐类型id */
	private String musicTypeId;
	/** 音乐类型标题 */
	private String musicTypeTitle;
	/** 音乐类型图片 */
	private String musicTypeImage;
	/** 音乐类型描述 */
	private String musicTypeDescription;

	public String getMusicTypeId() {
		return musicTypeId;
	}

	public void setMusicTypeId(String musicTypeId) {
		this.musicTypeId = musicTypeId;
	}

	public String getMusicTypeTitle() {
		return musicTypeTitle;
	}

	public void setMusicTypeTitle(String musicTypeTitle) {
		this.musicTypeTitle = musicTypeTitle;
	}

	public String getMusicTypeImage() {
		return musicTypeImage;
	}

	public void setMusicTypeImage(String musicTypeImage) {
		this.musicTypeImage = musicTypeImage;
	}

	public String getMusicTypeDescription() {
		return musicTypeDescription;
	}

	public void setMusicTypeDescription(String musicTypeDescription) {
		this.musicTypeDescription = musicTypeDescription;
	}

}
