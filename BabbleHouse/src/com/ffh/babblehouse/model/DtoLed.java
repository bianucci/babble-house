package com.ffh.babblehouse.model;

//TODO Remove this EXAMPLE class
public class DtoLed {

	private int id;
	private PinStatus pinStatus;
	
	public DtoLed(){
		pinStatus = PinStatus.LOW;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PinStatus getPinStatus() {
		return pinStatus;
	}
	public void setPinStatus(PinStatus pinStatus) {
		this.pinStatus = pinStatus;
	}
}
