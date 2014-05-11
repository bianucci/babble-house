package com.ffh.babblehouse.model;


import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DtoDevice{
	
	@Id 
	@GeneratedValue
	protected int id;
	String deviceName;
	List<DtoUDR> userDefineRules;
	List<DtoValue> values;

	public int getId() { 
		return id; 
	}
	
	public List<DtoValue> getValues() {
		return values;
	}

	public void setValues(List<DtoValue> values) {
		this.values = values;
	}
	
	public List<DtoUDR> getUserDefineRules() {
		return userDefineRules;
	}
	public void setUserDefineRules(List<DtoUDR> userDefineRules) {
		this.userDefineRules = userDefineRules;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
		

}
