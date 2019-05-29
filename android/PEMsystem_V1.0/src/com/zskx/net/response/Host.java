package com.zskx.net.response;

/**
 * IP HOST MODULE
 * @author wqp
 *
 */
public class Host {

	public final static String IP="ip";
	public final static String CITY="city";
	public final static String TAG="host";
	
	
	private String ip;
	private String city;

	
	
	public Host() {
		
	}


	public Host(String ip, String city) {
		this.ip = ip;
		this.city = city;
	}
	
	
	public String getIp() {
		return ip;
	}
	
	public String getCity() {
		return city;
	}
	
	
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setCity(String city) {
		this.city = city;
	}


	@Override
	public String toString() {
		return "Host [ip=" + ip + ", city=" + city + "]";
	}
	
	
	
}
