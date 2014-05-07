package com.ffh.babblehouse.database.entity;

import java.util.List;

public class Sensor {
	int id;
	String SensorName;
	List<UDRS> userDefineRules;
	List<Value> values;

	

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	public List<UDRS> getUserDefineRules() {
		return userDefineRules;
	}

	public void setUserDefineRules(List<UDRS> userDefineRules) {
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
