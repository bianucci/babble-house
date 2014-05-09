package com.ffh.babblehouse.model;

import java.sql.Timestamp;


public class DtoValue extends DtoBase {
	private int value;
	private Timestamp currentTimestamp;

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
