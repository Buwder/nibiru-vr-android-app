package com.zskx.testreport;

import java.io.Serializable;

public class ChoiceVO implements Serializable {

	private static final long serialVersionUID = -2806767245881092851L;

	private int childQuestionId;
	private String value;
	private String title;
	private String image;

//	public ChoiceVO(String value, String title, String image) {
//		super();
//		this.value = value;
//		this.title = title;
//		this.image = image;
//	}

//	public ChoiceVO(int childQuestionId, String value, String title, String image) {
//		super();
//		this.childQuestionId = childQuestionId;
//		this.value = value;
//		this.title = title;
//		this.image = image;
//	}

	public int getChildQuestionId() {
		return childQuestionId;
	}

	public void setChildQuestionId(int childQuestionId) {
		this.childQuestionId = childQuestionId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
