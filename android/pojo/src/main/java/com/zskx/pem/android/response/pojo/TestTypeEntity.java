package com.zskx.pem.android.response.pojo;

import java.io.Serializable;

public class TestTypeEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2797801922705882079L;
	/** 量表提交id */
	private long testItemId;
	/** 量表id */
	private long testQuestionId;
	/** 量表量表描述 */
	private String testQuestionDescription;
	/** 量表title */
	private String testQuestionTitle;
	/** 量表数字签名 */
	private String testSignatureImage;
	/** 量表阅读状态 */
	private String testStatus;
	private String testCoverImage;
	private String visible;
	
	
	
	
	
	


	public String getTestCoverImage() {
		return testCoverImage;
	}

	public void setTestCoverImage(String testCoverImage) {
		this.testCoverImage = testCoverImage;
	}

	public long getTestItemId() {
		return testItemId;
	}

	public void setTestItemId(long testItemId) {
		this.testItemId = testItemId;
	}

	public long getTestQuestionId() {
		return testQuestionId;
	}

	public void setTestQuestionId(long testQuestionId) {
		this.testQuestionId = testQuestionId;
	}

	public String getTestQuestionDescription() {
		return testQuestionDescription;
	}

	public void setTestQuestionDescription(String testQuestionDescription) {
		this.testQuestionDescription = testQuestionDescription;
	}

	public String getTestQuestionTitle() {
		return testQuestionTitle;
	}

	public void setTestQuestionTitle(String testQuestionTitle) {
		this.testQuestionTitle = testQuestionTitle;
	}

	public String getTestSignatureImage() {
		return testSignatureImage;
	}

	public void setTestSignatureImage(String testSignatureImage) {
		this.testSignatureImage = testSignatureImage;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	
}
