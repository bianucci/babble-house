package com.ffh.babblehouse.model;


import java.util.List;

public class DtoType extends DtoBase {
	String name;
	private List<DtoDevice> devices;
	
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
