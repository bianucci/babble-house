package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;

public interface IBoDtoChanges {
	void ChangeDeviceValues(DtoDevice dtoDevice);
	void ChangeSensorValues(DtoSensor dtoSensor);
}
