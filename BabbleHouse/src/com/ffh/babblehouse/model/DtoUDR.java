package com.ffh.babblehouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DtoUDR{
	@Id 
	@GeneratedValue
	protected int id;
	
	@ManyToOne
	private DtoDevice dtoDevice;
	
	@ManyToOne
	private DtoSensor dtoSensor;
	
	private int maxValueAllowed;
	
	private int minValueAllowed;
	
	private int greatMaxState;
	
	private int lessMinState;
	
	public Object[] toArray(){
		String componentName = verifyComponentType();
		String smallerThanMin = getActionText(this.getLessMinState());
		String greaterThanMax = getActionText(this.getGreatMaxState());
		
		return new Object[] { 
				Integer.valueOf(this.getMinValue()) , 
				Integer.valueOf(this.getMaxValue()), 
				componentName, 
				smallerThanMin,
				greaterThanMax };
	}
	
	private String verifyComponentType() {
		String componentName = "";
		if(this.getDtoDevice() != null )
			componentName = this.getDtoDevice().getDeviceName();
		else if(this.getDtoSensor() != null )
			componentName = this.getDtoSensor().getSensorName();
		return componentName;
	}

	private String getActionText(int value) {
		String text;
		if(value == 0)
			text = "Deactivate";
		else if(value == 1)
			text = "Activate";
		else  
			text = "Error in DB";
		return text;
	}
	//region Getters and Setters
	
	public int getId() { 
		return id; 
	}
	
	public int getMaxValue() {
		return maxValueAllowed;
	}

	public void setMaxValue(int maxValue) {
		this.maxValueAllowed = maxValue;
	}

	public int getMinValue() {
		return minValueAllowed;
	}

	public void setMinValue(int minValue) {
		this.minValueAllowed = minValue;
	}

	public int getGreatMaxState() {
		return greatMaxState;
	}

	public void setGreatMaxState(int greatMaxState) {
		this.greatMaxState = greatMaxState;
	}

	public int getLessMinState() {
		return lessMinState;
	}

	public void setLessMinState(int lessMinState) {
		this.lessMinState = lessMinState;
	}

	public DtoDevice getDtoDevice() {
		return dtoDevice;
	}

	public void setDtoDevice(DtoDevice dtoDevice) {
		this.dtoDevice = dtoDevice;
	}

	public DtoSensor getDtoSensor() {
		return dtoSensor;
	}

	public void setDtoSensor(DtoSensor dtoSensor) {
		this.dtoSensor = dtoSensor;
	}
	
	//endregion Getters and Setters
}
