package com.zskx.pemsystem.util;

/**
 * 收入类型menu
 * */
public enum EnumIncome {
	BELOW_TEN_HUN("BELOW_TEN_HUN", "1000元以下"),
	TEN_TO_TWENTY_HUN("TEN_TO_TWENTY_HUN", "1000-2000元"),
	TWENTY_TO_THIRTY_HUN("TWENTY_TO_THIRTY_HUN", "2000-3000元"),
	THIRTY_TO_FIFTY_HUN("THIRTY_TO_FIFTY_HUN", "3000-5000元"),
	ABOVE_FIFTY_HUN("ABOVE_FIFTY_HUN", "5000元以上");
	
	private final String value;
	private final String title;

	EnumIncome(String value, String title) {
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
