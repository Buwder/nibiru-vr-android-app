package com.zskx.testreport;

/**
 * 性别枚举<br />
 * MALE 男<br />
 * FEMALE 女<br />
 * @author chenjun
 *
 */
public enum EnumSex {
	MALE("MALE", "男"),
	FEMALE("FEMALE", "女"),
	OTHER("OTHER", "其它");

	private final String value;
	private final String title;

	EnumSex(String value, String title) {
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
