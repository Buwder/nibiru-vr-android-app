package com.zskx.pemsystem.ebook;

public class Directory extends BookMark {
	/**
	 * 目录应该跳转到的位置标志
	 */
	private String dir_tag;

	public String getDir_tag() {
		return dir_tag;
	}

	public void setDir_tag(String dir_tag) {
		this.dir_tag = dir_tag;
	}

	@Override
	public String toString() {
		return "Directory [dir_tag=" + dir_tag + "," + super.toString();
	}

}
