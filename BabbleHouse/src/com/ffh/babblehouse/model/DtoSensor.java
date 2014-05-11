package com.ffh.babblehouse.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class DtoSensor {
	@Id 
	@GeneratedValue
	protected int id;
	
	String SensorName;
	
	@OneToMany(cascade= CascadeType.REMOVE)
	List<DtoUDR> userDefineRules;
	
	// TODO Make sure it is OneToMany instead of One to One
	@OneToMany(cascade= CascadeType.REMOVE)
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

	public String getSensorName() {
		return SensorName;
	}

	public void setSensorName(String sensorName) {
		SensorName = sensorName;
	}

	
}
