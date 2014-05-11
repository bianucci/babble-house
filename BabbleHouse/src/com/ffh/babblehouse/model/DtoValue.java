package com.ffh.babblehouse.model;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DtoValue {
	@Id 
	@GeneratedValue
	protected int id;
	
	private int value;
	private Timestamp currentTimestamp;

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
