package com.zskx.pemsystem.ebook;

import java.util.LinkedList;

public class MagazineBean {

	public static int NONE = 0;

	public static int TITLE = 1;

	public static int DIRECTORY = 2;

	public static int CONTENT = 3;

	private String title;

	private LinkedList<Directory> directory = new LinkedList<Directory>();

	private LinkedList<Bold> bold_list = new LinkedList<Bold>();

	private StringBuilder content = new StringBuilder();

	public void addBold(Bold bold) {
		bold_list.add(bold);
	}

	public LinkedList<Bold> getBold_list() {
		return bold_list;
	}

	public void addDir(Directory dir) {
		directory.add(dir);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public StringBuilder getContent() {
		return content;
	}

	public LinkedList<Directory> getDirectory() {
		return directory;
	}

	public void setDirectory(LinkedList<Directory> directory) {
		this.directory = directory;
	}

	public void addContent(String content) {
		this.content.append(content);
	}

	@Override
	public String toString() {
		return "MagazineBean [title=" + title + ", directory=" + directory
				+ ",bold_list=" + bold_list + ", content=" + content.toString()
				+ "]";
	}

}
