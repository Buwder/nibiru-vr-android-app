package com.zskx.net.test;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class MagazineTypeEntity implements Serializable {

	/** 杂志类型Id */
	private String magazineTypeId;
	/** 杂志类型标题 */
	private String magazineTypeTitle;
	/** 杂志类型图标Url */
	private String magazineTypeIconUrl;

	public String getMagazineTypeId() {
		return magazineTypeId;
	}

	public void setMagazineTypeId(String magazineTypeId) {
		this.magazineTypeId = magazineTypeId;
	}

	public String getMagazineTypeTitle() {
		return magazineTypeTitle;
	}

	public void setMagazineTypeTitle(String magazineTypeTitle) {
		this.magazineTypeTitle = magazineTypeTitle;
	}

	public String getMagazineTypeIconUrl() {
		if (magazineTypeIconUrl.startsWith(NetConfiguration.DOWNLOAD_ADD)
				|| "".equals(magazineTypeIconUrl)) {
			return magazineTypeIconUrl;
		} else {
			return NetConfiguration.DOWNLOAD_ADD + magazineTypeIconUrl;
		}
	}

	public void setMagazineTypeIconUrl(String magazineTypeIconUrl) {
		magazineTypeIconUrl = magazineTypeIconUrl;
	}

}
