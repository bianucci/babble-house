package com.ffh.babblehouse.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
@Entity
public class DtoValue {
	@Id 
	@GeneratedValue
	protected int id;
	
	private int value;
	private Timestamp currentTimestamp;
	
	@ManyToOne
	private DtoDevice dtoDevice;
	
	public DtoDevice getDtoDevice() {
		return dtoDevice;
	}

	public void setDtoDevice(DtoDevice dtoDevice) {
		this.dtoDevice = dtoDevice;
	}

	public int getId() { 
		return id; 
	}
	
	public Timestamp getCurrentTimestamp() {
		return currentTimestamp;
	}
	
	public void setCurrentTimestamp(Timestamp currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
