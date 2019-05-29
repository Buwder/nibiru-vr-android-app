package com.zskx.net.response;

import java.io.Serializable;

public class GameTypeEntity implements Serializable {

	/** 游戏类型Id */
	private String gameTypeId;
	/** 游戏类型标题 */
	private String gameTypeTitle;

	public String getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(String gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getGameTypeTitle() {
		return gameTypeTitle;
	}

	public void setGameTypeTitle(String gameTypeTitle) {
		this.gameTypeTitle = gameTypeTitle;
	}

}
