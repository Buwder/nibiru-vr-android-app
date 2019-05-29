package com.zskx.net.response;

import com.zskx.testreport.SelectQuestionParsedVO;
import java.io.Serializable;

public class TestQuestionVo implements Serializable {
	private static final long serialVersionUID = -3746510321813859013L;
	private int questionAmount;
	private int testItemId;
	private int testQuestionId;
	private SelectQuestionParsedVO vo;

	public int getQuestionAmount() {
		return this.questionAmount;
	}

	public int getTestItemId() {
		return this.testItemId;
	}

	public int getTestQuestionId() {
		return this.testQuestionId;
	}

	public SelectQuestionParsedVO getVo() {
		return this.vo;
	}

	public void setQuestionAmount(int paramInt) {
		this.questionAmount = paramInt;
	}

	public void setTestItemId(int paramInt) {
		this.testItemId = paramInt;
	}

	public void setTestQuestionId(int paramInt) {
		this.testQuestionId = paramInt;
	}

	public void setVo(SelectQuestionParsedVO paramSelectQuestionParsedVO) {
		this.vo = paramSelectQuestionParsedVO;
	}

	public String toString() {
		return "TestQuestionVo [testQuestionId=" + this.testQuestionId
				+ ", questionAmount=" + this.questionAmount + ", testItemId="
				+ this.testItemId + ", vo=" + this.vo.toString() + "]";
	}
}

/*
 * Location:
 * /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem
 * -1/classes_dex2jar.jar Qualified Name: com.zskx.net.response.TestQuestionVo
 * JD-Core Version: 0.5.4
 */