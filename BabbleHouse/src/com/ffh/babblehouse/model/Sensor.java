package com.ffh.babblehouse.model;

import java.util.List;

public class Sensor {
	int id;
	String SensorName;
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

	public String getSensorName() {
		return SensorName;
	}

	public void setSensorName(String sensorName) {
		SensorName = sensorName;
	}

	
}
