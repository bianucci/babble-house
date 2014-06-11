package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;

public interface IBoStateChangedHandler {

	void sensorDataChanged(DtoSensor updatedDtoSensor);
	void deviceDataChanged(DtoDevice updatedDtoDevice);
	
}
