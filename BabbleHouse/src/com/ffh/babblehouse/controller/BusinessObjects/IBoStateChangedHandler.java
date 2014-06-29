package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;

public interface IBoStateChangedHandler {

	void sensorDataChanged(DtoSensor updatedDtoSensor);
	void deviceDataChanged(DtoDevice updatedDtoDevice);
	void newServiceGroupArrived(DtoServiceGroup newServiceGroup);

}
