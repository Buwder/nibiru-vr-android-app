package com.zskx.pem.android.response.pojo;

import java.io.Serializable;
import java.util.Date;

public class ReportEntity implements Serializable {
	/** 测试报告的Id */
	private String testItemId;
	/** 报告名称 */
	private String reportTitle;
	/** 报告状态 */
	private String reportStatus;
	/** 报告时间 */
	private Date reportTime;
	/** 报告简介 */
	private String reportInstroduction;

	

	public String getTestItemId() {
		return testItemId;
	}

	public void setTestItemId(String testItemId) {
		this.testItemId = testItemId;
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

}
