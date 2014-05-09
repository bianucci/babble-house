package com.ffh.babblehouse.model;

import java.util.List;

public class DtoGateway {
private int Baudrate;
private int Stopbits;
private int Databits;
private int Parity_none;
List<DtoServiceGroup> serviceGroups ;

public List<DtoServiceGroup> getServiceGroups() {
	return serviceGroups;
}
public void setServiceGroups(List<DtoServiceGroup> controllers) {
	this.serviceGroups = controllers;
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
public void addSensor(DtoServiceGroup this_controller){
	serviceGroups.add(this_controller);
}

// remove controllers from list
public void removeDevice(DtoServiceGroup this_controller){
	serviceGroups.remove(this_controller);
	}




}
