package com.ffh.babblehouse.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DtoMeasuringUnit {

	@Id
	@GeneratedValue
	protected int id;

	private String unit_name;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<DtoSensor> sensors;

	public int getId() {
		return id;
	}

	public List<DtoSensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<DtoSensor> sensors) {
		this.sensors = sensors;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	// add new sensor to list
	public void addSensor(DtoSensor this_sensor) {
		sensors.add(this_sensor);
	}

	// remove sensor from list
	public void removeDevice(DtoSensor this_sensor) {
		sensors.remove(this_sensor);
	}

	@Override
	public String toString() {
		return "DtoMeasuringUnit [id=" + id + ", unit_name=" + unit_name + "]";
	}

}
