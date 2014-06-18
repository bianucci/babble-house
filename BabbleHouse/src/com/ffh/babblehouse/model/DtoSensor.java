package com.ffh.babblehouse.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DtoSensor {
	@Id
	@GeneratedValue
	protected int id;

	String SensorName;

	@ManyToOne
	DtoMeasuringUnit measuringUnit;

	@ManyToOne
	DtoServiceGroup dtoServiceGroup;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoUDR> userDefineRules;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoValue> values;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoUDR> dtoUDR;

	// region Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DtoMeasuringUnit getMeasuringUnit() {
		return measuringUnit;
	}

	public void setMeasuringUnit(DtoMeasuringUnit measuringUnit) {
		this.measuringUnit = measuringUnit;
	}

	public List<DtoValue> getValues() {
		return values;
	}

	public void setValues(List<DtoValue> values) {
		this.values = values;
	}

	public DtoValue getLatestValue() {
		if (this.values.size() > 0)
			return this.values.get(this.values.size() - 1);
		return new DtoValue();
	}

	public List<DtoUDR> getUserDefineRules() {
		return userDefineRules;
	}

	public void setUserDefineRules(List<DtoUDR> userDefineRules) {
		this.userDefineRules = userDefineRules;
	}

	public String getSensorName() {
		return SensorName;
	}

	public void setSensorName(String sensorName) {
		SensorName = sensorName;
	}

	public DtoServiceGroup getDtoServiceGroup() {
		return dtoServiceGroup;
	}

	public void setDtoServiceGroup(DtoServiceGroup dtoServiceGroup) {
		this.dtoServiceGroup = dtoServiceGroup;
	}

	// endregion Getters and Setters

	public void addValue(DtoValue dtoValue) {
		if (dtoValue != null)
			values.add(dtoValue);
		dtoValue.setDtoSensor(this);
	}

	public void addUDR(DtoUDR dtoUDR) {
		if (dtoUDR != null) {
			dtoUDR.setDtoSensor(this);
			this.dtoUDR.add(dtoUDR);
		}
	}

	@Override
	public String toString() {
		return "DtoSensor [id=" + id + ", SensorName=" + SensorName
				+ ", measuringUnit=" + measuringUnit + ", dtoServiceGroupID="
				+ dtoServiceGroup.getId() + ", nuberOfUserDefineRules="
				+ userDefineRules.size() + ", lastValueMeassured="
				+ getLatestValue() + "]";
	}

}
