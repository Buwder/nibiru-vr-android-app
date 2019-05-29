package com.zskx.pemsystem.util;

/**
 * 婚姻状况枚举<br />
 * MARRIED 已婚<br />
 * UNMARRIED 未婚<br />
 * 
 * @author chenjun
 * 
 */
public enum EnumMarriage {
	MARRIED("MARRIED", "已婚"), UNMARRIED("UNMARRIED", "未婚"), OTHER("OTHER", "其它");

	private final String value;
	private final String title;

	EnumMarriage(String value, String title) {
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
