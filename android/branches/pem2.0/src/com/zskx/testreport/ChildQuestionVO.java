package com.zskx.testreport;

import java.io.Serializable;


/**
 * 子题VO
 * @author chenjun
 *
 */
public class ChildQuestionVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -926422671646327605L;

	private int id;
	private String title;
	private EnumChoiceType type;
	private int choiceNumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EnumChoiceType getType() {
		return type;
	}

	public void setType(EnumChoiceType type) {
		this.type = type;
	}

	public int getChoiceNumber() {
		return choiceNumber;
	}

	public void setChoiceNumber(int choiceNumber) {
		this.choiceNumber = choiceNumber;
	}

}