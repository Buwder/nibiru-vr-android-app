package com.zskx.pemsystem.ebook;

public class BookMark {
	/**
	 * 书签的名字
	 */
	protected String name;
	/**
	 * 书签的位置
	 */
	protected int position;
	/**
	 * 书签的内容
	 */
	protected String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "BookMark [name=" + name + ",position=" + position + ",content="
				+ content + "]";
	}

}
