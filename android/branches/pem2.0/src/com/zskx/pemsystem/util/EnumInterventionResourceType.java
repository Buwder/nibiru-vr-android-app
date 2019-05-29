/* *
 * ============================================================================
 * <p>Title: PEM: Psychological Evalution and Management，PEM心里体检管理平台。</p>
 * <p>Company: 中盛凯欣技术发展有限公司。</p>
 * <p>Copyright: 2012中盛凯欣企业集团|IPEC研究中心，版权所有。 </p>
 * @(#)EnumIntercentionGroupType.java 2012-6-5
 * © 2012 Xi'an ZSKX Technology Development Co.,Ltd, All Right Reserved.  ©
 * ============================================================================
 */
package com.zskx.pemsystem.util;

import java.util.HashMap;
import java.util.Map;

/**
 * EXP: 干预套餐枚举類型：积极心里，减压系列，音乐治疗，心里训练，心里游戏
 * 
 * @author <a href="mailto:springsfeng@sina.com">FDC</a>
 * @version 1.0
 * @2012-6-5
 * @since 2012-6-5
 */
public enum EnumInterventionResourceType {

	MUSIC("MUSIC", "音乐治疗"), DECOMPRESSANIME("DECOMPRESSANIME", "放松减压"), MAGAZINE(
			"MAGAZINE", "积极心理"), TRAINANIME("TRAINANIME", "心理训练"), GAME("GAME",
			"心理游戏"), ARTICLE("ARTICLE", "心灵学堂");

	private final String value;
	private final String title;

	EnumInterventionResourceType(String value, String title) {
		this.value = value;
		this.title = title;
	}

	private static Map<String, EnumInterventionResourceType> typeMap = new HashMap<String, EnumInterventionResourceType>();

	static {

		EnumInterventionResourceType[] types = EnumInterventionResourceType
				.values();
		for (EnumInterventionResourceType type : types) {
			typeMap.put(type.title, type);
		}
	}

	public static EnumInterventionResourceType titleOf(String title) {
		return typeMap.get(title);
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