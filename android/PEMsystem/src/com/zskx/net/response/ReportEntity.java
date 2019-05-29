package com.zskx.net.response;

import java.io.Serializable;
import java.util.Date;

public class ReportEntity implements Serializable {
	/** 报告的Id */
	private String reportId;
	/** 报告名称 */
	private String reportTitle;
	/** 报告状态 */
	private String reportStatus;
	/** 报告时间 */
	private Date reportTime;
	/** 报告简介 */
	private String reportInstroduction;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getReportInstroduction() {
		return reportInstroduction;
	}

	public void setReportInstroduction(String reportInstroduction) {
		this.reportInstroduction = reportInstroduction;
	}

	@Override
	public String toString() {
		return "ReportEntity [reportId=" + reportId + ", reportTitle="
				+ reportTitle + ", reportStatus=" + reportStatus
				+ ", reportTime=" + reportTime + ", reportInstroduction="
				+ reportInstroduction + "]";
	}

}
