package com.zskx.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ReportDetailEntity implements Serializable {
	/** 报告的Id */
	private String reportId;
	/** 报告名称 */
	private String reportTitle;
	/** 报告状态 */
	private String reportStatus;
	/** 报告时间 */
	private Date reportTime;
	/** 报告结果图片（可多个） */
	private ArrayList<String> reportResultImage;
	/** 报告结果文字描述 */
	private ArrayList<String> reportResultText;
	/** 报告评语 */
	private String reportComment;
	/** 报告建议 */
	private String reportSuggestion;

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

	public ArrayList<String> getReportResultImage() {
		return reportResultImage;
	}

	public void setReportResultImage(ArrayList<String> reportResultImage) {
		this.reportResultImage = reportResultImage;
	}

	public ArrayList<String> getReportResultText() {
		return reportResultText;
	}

	public void setReportResultText(ArrayList<String> reportResultText) {
		this.reportResultText = reportResultText;
	}

	public String getReportComment() {
		return reportComment;
	}

	public void setReportComment(String reportComment) {
		this.reportComment = reportComment;
	}

	public String getReportSuggestion() {
		return reportSuggestion;
	}

	public void setReportSuggestion(String reportSuggestion) {
		this.reportSuggestion = reportSuggestion;
	}

	@Override
	public String toString() {
		return "ReportDetailEntity [reportId=" + reportId + ", reportTitle="
				+ reportTitle + ", reportStatus=" + reportStatus
				+ ", reportTime=" + reportTime + ", reportResultImage="
				+ reportResultImage + ", reportResultText=" + reportResultText
				+ ", reportComment=" + reportComment + ", reportSuggestion="
				+ reportSuggestion + "]";
	}

}
