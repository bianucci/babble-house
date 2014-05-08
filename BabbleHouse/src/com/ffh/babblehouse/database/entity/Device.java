package com.ffh.babblehouse.database.entity;


import java.util.List;

public class Device {
	int id;
	String deviceName;
	List<UDR> userDefineRules;
	List<Value> values;

	
	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}
	
	public List<UDR> getUserDefineRules() {
		return userDefineRules;
	}
	public void setUserDefineRules(List<UDR> userDefineRules) {
		this.userDefineRules = userDefineRules;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
		

}
