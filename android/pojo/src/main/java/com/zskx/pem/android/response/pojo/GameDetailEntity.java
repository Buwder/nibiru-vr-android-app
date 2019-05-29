package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

public class GameDetailEntity implements Serializable {

	/** 视频Id */
	private String gameId;
	/** 视频类型 */
	private String gameType;
	/** 视频图片 */
	private String gameImage;
	/** 视频标题 */
	private String gameTitle;
	/** 视频网络地址 */
	private String gameUrl;

	public String getgameId() {
		return gameId;
	}

	public void setgameId(String gameId) {
		this.gameId = gameId;
	}

	public String getgameType() {
		return gameType;
	}

	public void setgameType(String gameType) {
		this.gameType = gameType;
	}

	public String getgameImage() {
		return gameImage;
	}

	public void setgameImage(String gameImage) {
		this.gameImage = gameImage;
	}

	public String getgameTitle() {
		return gameTitle;
	}

	public void setgameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getgameUrl() {
		return gameUrl;
	}

	public void setgameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}

}
