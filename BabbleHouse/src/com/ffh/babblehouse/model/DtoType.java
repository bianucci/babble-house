package com.ffh.babblehouse.model;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class DtoType {
	@Id 
	@GeneratedValue
	protected int id;
	
	String name;
	private List<DtoDevice> devices;
	
	public int getId() { 
		return id; 
	}
	
	public List<DtoDevice> getDevices() {
		return devices;
	}
	public void setDevices(List<DtoDevice> devices) {
		this.devices = devices;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
