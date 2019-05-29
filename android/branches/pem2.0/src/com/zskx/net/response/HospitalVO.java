package com.zskx.net.response;

import java.io.Serializable;

import android.location.Location;

public class HospitalVO implements Serializable{


	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public HospitalVO(String name, String city, String department,
			String address, String phoneNumber, String doctor, double latitude,
			double longitude) {
		super();
		this.name = name;
		this.city = city;
		this.department = department;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.doctor = doctor;
		this.latitude = latitude;
		Longitude = longitude;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1669986105901944620L;
	
	private String name;
	private String city;
	private String department;
	private String address;
	private String phoneNumber;
	private String doctor;
	private double latitude;
	private double Longitude;
	private double distance = 0;
}
