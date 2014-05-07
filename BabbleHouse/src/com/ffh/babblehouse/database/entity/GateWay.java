package com.ffh.babblehouse.database.entity;

import java.util.List;



public class GateWay {
private int Baudrate;
private int Stopbits;
private int Databits;
private int Parity_none;
List<Controller> controllers ;


public List<Controller> getControllers() {
	return controllers;
}
public void setControllers(List<Controller> controllers) {
	this.controllers = controllers;
}
public int getBaudrate() {
	return Baudrate;
}
public void setBaudrate(int baudrate) {
	Baudrate = baudrate;
}
public int getStopbits() {
	return Stopbits;
}
public void setStopbits(int stopbits) {
	Stopbits = stopbits;
}
public int getDatabits() {
	return Databits;
}
public void setDatabits(int databits) {
	Databits = databits;
}
public int getParity_none() {
	return Parity_none;
}

public void setParity_none(int parity_none) {
	Parity_none = parity_none;
}

//add new controllers to list
public void addSensor(Controller this_controller){
	controllers.add(this_controller);
}

// remove controllers from list
public void removeDevice(Controller this_controller){
	controllers.remove(this_controller);
	}




}
