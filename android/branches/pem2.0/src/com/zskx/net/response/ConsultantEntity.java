package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class ConsultantEntity implements Serializable {

	private static final long serialVersionUID = 1193921059371192014L;
	private String consultantImage;
	private String consultantName;
	private String consultantDescription;
	private String consultantStatus; //是否在线

	public String getConsultantImage() {
		
		 if (consultantImage.startsWith(NetConfiguration.DOWNLOAD_ADD)
					|| "".equals(consultantImage)) {
				return consultantImage;
			} else {
				return NetConfiguration.DOWNLOAD_ADD + consultantImage;
			}
	}

	public void setConsultantImage(String consultantImage) {
		this.consultantImage = consultantImage;
	}
	
	
	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public String getConsultantDescription() {
		return consultantDescription;
	}

	public void setConsultantDescription(String consultantDescription) {
		this.consultantDescription = consultantDescription;
	}

	public String getConsultantStatus() {
		return consultantStatus;
	}

	public void setConsultantStatus(String consultantStatus) {
		this.consultantStatus = consultantStatus;
	}
	

}
