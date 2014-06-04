package com.ffh.babblehouse.controller.BusinessObjects;

import java.util.List;

import com.ffh.babblehouse.controller.repositories.SensorRepository;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoValue;

public class BoSensor extends BoBase<DtoSensor>{

	public BoSensor(){
		this.repository = new SensorRepository();
	}
	
	public List<DtoValue> getLastSensorValuesById(int sensorId, int amountOfRecords){
		return this.getLastSensorValuesById(sensorId, amountOfRecords);
	}


	public DtoSensor getSensorByName(String sensorName,int amountOfRecords) {
		return ((SensorRepository) this.repository).getSensorByName(sensorName,amountOfRecords); 
	}
	
}
