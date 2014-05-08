package com.ffh.babblehouse.database.entity;

import java.util.List;

public class Controller {

	int id;
	private String name;
	private int status; // 1 means on and 2 means off
	private List<Device> devices;
	private List<Sensor> sensors;
	private int batterylevel; // percentage

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getBatterylevel() {
		return batterylevel;
	}

	public void setBatterylevel(int batterylevel) {
		this.batterylevel = batterylevel;
	}

}
