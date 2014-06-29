package com.ffh.babblehouse.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DtoDevice {

	@Id
	@GeneratedValue
	protected int id;

	String deviceName;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoUDR> userDefineRules;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoValue> values;

	@ManyToOne
	DtoType type;

	@ManyToOne
	DtoServiceGroup dtoServiceGroup;

	@OneToMany(cascade = CascadeType.ALL)
	List<DtoUDR> dtoUDR;

	public DtoServiceGroup getDtoServiceGroup() {
		return dtoServiceGroup;
	}

	public void setDtoServiceGroup(DtoServiceGroup dtoServiceGroup) {
		this.dtoServiceGroup = dtoServiceGroup;
	}

	// region Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DtoType getType() {
		return type;
	}

	public void setType(DtoType type) {
		this.type = type;
	}

	public List<DtoValue> getValues() {
		return values;
	}

	public void setValues(List<DtoValue> values) {
		this.values = values;
	}

	public DtoValue getLatestValue() {
		if (values != null)
			if (this.values.size() > 0)
				return this.values.get(this.values.size() - 1);

		DtoValue dtoValue = new DtoValue();
		dtoValue.setValue(-1);
		return dtoValue;
	}

	public List<DtoUDR> getUserDefineRules() {
		return userDefineRules;
	}

	public void setUserDefineRules(List<DtoUDR> userDefineRules) {
		this.userDefineRules = userDefineRules;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	// endregion Getters and Setters

	public void addValue(DtoValue dtoValue) {
		if (dtoValue != null) {
			dtoValue.setDtoDevice(this);
			values.add(dtoValue);
		}
	}

	public void addUDR(DtoUDR dtoUDR) {
		if (dtoUDR != null) {
			dtoUDR.setDtoDevice(this);
			this.dtoUDR.add(dtoUDR);
		}
	}

	@Override
	public String toString() {
		int idSG = 255;
		if (getDtoServiceGroup() != null)
			idSG = getDtoServiceGroup().getId();

		int size = 255;
		if (userDefineRules != null)
			size = userDefineRules.size();

		return "DtoDevice [id=" + id + ", DeviceName=" + deviceName
				+ ", dtoServiceGroupID=" + idSG + ", nuberOfUserDefineRules="
				+ size + ", currentState=" + getLatestValue() + "]";
	}
}