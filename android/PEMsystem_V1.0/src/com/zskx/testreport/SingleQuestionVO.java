package com.zskx.testreport;

import java.io.Serializable;
import java.util.List;


public class SingleQuestionVO implements Serializable {

	private static final long serialVersionUID = 558813301352650485L;

	// 题号
	private int number;
	// 问题
	private String title;
	// 性别 boy, girl 转化为枚举MALE， FEMALE 题跟性别无关则为null
	private EnumSex sex;
	// 单选 多选
	private EnumChoiceType type;
	private String questionImage;
	// key:  选项值， value: 选项title
	private List<ChoiceVO> choices;
	// 子题列表
	private List<ChildQuestionVO> childQuestions;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EnumSex getSex() {
		return sex;
	}

	public void setSex(EnumSex sex) {
		this.sex = sex;
	}

	public EnumChoiceType getType() {
		return type;
	}

	public void setType(EnumChoiceType type) {
		this.type = type;
	}

	public String getQuestionImage() {
		return questionImage;
	}

	public void setQuestionImage(String questionImage) {
		this.questionImage = questionImage;
	}

	public List<ChoiceVO> getChoices() {
		return choices;
	}

	public void setChoices(List<ChoiceVO> choices) {
		this.choices = choices;
	}

	public List<ChildQuestionVO> getChildQuestions() {
		return childQuestions;
	}

	public void setChildQuestions(List<ChildQuestionVO> childQuestions) {
		this.childQuestions = childQuestions;
	}

}
