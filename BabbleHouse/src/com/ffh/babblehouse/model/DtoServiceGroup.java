package com.ffh.babblehouse.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DtoServiceGroup {
	@Id
	@GeneratedValue
	protected int id;

	private String name;
	private int status; // 1 means on and 2 means off

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<DtoDevice> devices;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<DtoSensor> sensors;

	private int batterylevel; // percentage

	public DtoServiceGroup() {
		devices = new ArrayList<DtoDevice>();
		sensors = new ArrayList<DtoSensor>();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<DtoDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<DtoDevice> devices) {
		this.devices = devices;
	}

	public List<DtoSensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<DtoSensor> sensors) {
		this.sensors = sensors;
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

	@Override
	public String toString() {
		return "DtoServiceGroup [id=" + id + ", name=" + name + ", status="
				+ status + ", devices=" + devices + ", sensors=" + sensors
				+ ", batterylevel=" + batterylevel + "]";
	}

	public String asFancyString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DtoServiceGroup [\n");
		sb.append("   id=" + id + "\n");
		sb.append("   name=" + name + "\n");
		sb.append("   status=" + status + "\n");
		sb.append("   batterylevel=" + batterylevel + "\n");
		sb.append("   Sensors [" + "\n");
		for (DtoSensor s : getSensors()) {
			sb.append("     id=" + s.getId());
			sb.append("  unit=" + s.getSensorName() + "\n");
		}
		sb.append("   ]" + "\n");
		sb.append("   Actuators [" + "\n");
		for (DtoDevice d : getDevices()) {
			sb.append("     id=" + d.getId());
			sb.append("   name=" + d.getDeviceName() + "\n");
		}
		sb.append("   ]" + "\n");
		return sb.toString();
	}
}
