package com.zskx.testreport;

/**
 * 测试题 单选 多选
 * @author chenjun
 *
 */
public enum EnumChoiceType {
	SINGLE("SINGLE", "单选"),
	MORE("MORE", "多选"),
	NUMBERAXIS("NUMBERAXIS", "数轴"),
	MUTEX("MUTEX", "子选项互斥类");

	private final String value;
	private final String title;

	EnumChoiceType(String value, String title) {
		this.value = value;
		this.title = title;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}

}