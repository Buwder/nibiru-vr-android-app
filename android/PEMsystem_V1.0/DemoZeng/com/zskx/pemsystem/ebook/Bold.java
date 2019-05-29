package com.zskx.pemsystem.ebook;

public class Bold {
	/**
	 * 需要加粗的内容
	 */
	protected String content;
	/**
	 * 加粗内容的第一个字的位置
	 */
	protected int start_position;

	protected int end_position;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStart_position() {
		return start_position;
	}

	public void setStart_position(int start_position) {
		this.start_position = start_position;
	}

	public int getEnd_position() {
		return end_position;
	}

	public void setEnd_position(int end_position) {
		this.end_position = end_position;
	}

	@Override
	public String toString() {
		return "Bold [content=" + content + ", start_position="
				+ start_position + ", end_position=" + end_position + "]";
	}

}
