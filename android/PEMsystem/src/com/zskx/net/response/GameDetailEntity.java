package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

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
		if (gameImage.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(gameImage)) {
			return gameImage;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + gameImage;
		}
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
		if (gameUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(gameUrl)) {
			return gameUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + gameUrl;
		}
	}

	public void setgameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}

}
