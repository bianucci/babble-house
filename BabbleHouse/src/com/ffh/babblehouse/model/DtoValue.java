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

	@ManyToOne
	private DtoSensor dtoSensor;

	public DtoDevice getDtoDevice() {
		return dtoDevice;
	}

	public void setDtoDevice(DtoDevice dtoDevice) {
		if (dtoSensor != null)
			dtoSensor = null;
		this.dtoDevice = dtoDevice;
	}

	public void setDtoSensor(DtoSensor dtoSensor) {
		if (dtoDevice != null)
			dtoDevice = null;
		this.dtoSensor = dtoSensor;
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

	@Override
	public String toString() {
		String belongsTo = null;
		if (this.getDtoDevice() != null) {
			belongsTo = "device";
		} else {
			belongsTo = "sensor";
		}
		return "DtoValue [id=" + id + ", value=" + value
				+ ", currentTimestamp=" + currentTimestamp + ", type="
				+ belongsTo + "]";
	}
}
