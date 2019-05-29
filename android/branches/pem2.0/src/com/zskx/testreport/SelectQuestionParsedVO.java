package com.zskx.testreport;

import java.io.Serializable;
import java.util.List;


public class SelectQuestionParsedVO implements Serializable {

	private static final long serialVersionUID = 485807517720389825L;

	private long questionId;
	private String title;
	private String description;
	private String guide;
	private String backgroundMusic;
	private int questionForMaleAmount;
	private int questionForFemaleAmount;

	private List<SingleQuestionVO> listSelectQuestion;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public String getBackgroundMusic() {
		return backgroundMusic;
	}

	public void setBackgroundMusic(String backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	public List<SingleQuestionVO> getListSelectQuestion() {
		return listSelectQuestion;
	}

	public void setListSelectQuestion(List<SingleQuestionVO> listSelectQuestion) {
		this.listSelectQuestion = listSelectQuestion;
	}

	public void setQuestionForMaleAmount(int questionForMaleAmount) {
		this.questionForMaleAmount = questionForMaleAmount;
	}

	public void setQuestionForFemaleAmount(int questionForFemaleAmount) {
		this.questionForFemaleAmount = questionForFemaleAmount;
	}

	public int getQuestionForMaleAmount() {
		return questionForMaleAmount;
	}

	public int getQuestionForFemaleAmount() {
		return questionForFemaleAmount;
	}

	public int getQuestionAmount(EnumSex sex) {
		if (sex.equals(EnumSex.MALE)) {
			return questionForMaleAmount;
		} else {
			return questionForFemaleAmount;
		}
	}

}