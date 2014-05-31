package com.ffh.babblehouse.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DtoDevice{
	
	@Id 
	@GeneratedValue
	protected int id;
	String deviceName;
	
	@OneToMany(cascade= CascadeType.REMOVE)
	List<DtoUDR> userDefineRules;
	
	@OneToMany(cascade= CascadeType.REMOVE)
	List<DtoValue> values;

	public int getId() { 
		return id; 
	}
	public void setID(int id){
	this.id=id;	
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
