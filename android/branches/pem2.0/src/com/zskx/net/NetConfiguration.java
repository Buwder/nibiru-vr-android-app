package com.zskx.net;

public class NetConfiguration {


	
	public static String NAME_SPACE = "";

	public static final String CLIENT_OS = "android";
	public static String mMapKey = "C0771E6860F08B179ECD7BA02745D4691586B89D";
	
//	public static  String DOWNLOAD_ADD = "http://192.168.0.230/";
//	public static  String IMAGE_DOWNLOAD_ADD = "http://192.168.0.230/mobile/";
	
	public static  String DOWNLOAD_ADD = "http://img.pems.cn/";
	public static  String IMAGE_DOWNLOAD_ADD = "http://img.pems.cn/mobile/";
	
	public static String getDOWNLOAD_ADD() {
		return DOWNLOAD_ADD;
	}
	public static void setDOWNLOAD_ADD(String dOWNLOAD_ADD) {
		DOWNLOAD_ADD = dOWNLOAD_ADD;
	}
}
