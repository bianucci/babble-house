package com.ffh.babblehouse.database.entity;

import java.util.List;

public class MeausuringUnit {
private int id;

private String unit_name;
private List<Sensor> sensors;


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
public String getUnit_name() {
	return unit_name;
}
public void setUnit_name(String unit_name) {
	this.unit_name = unit_name;
}

//add new sensor to list
public void addSensor(Sensor this_sensor){
sensors.add(this_sensor);
}

// remove sensor from list
public void removeDevice(Sensor this_sensor){
	sensors.remove(this_sensor);
	}


}
