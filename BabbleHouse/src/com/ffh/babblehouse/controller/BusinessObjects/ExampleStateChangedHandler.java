package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.IChangeReceiver;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;

public class ExampleStateChangedHandler implements IBoStateChangedHandler {
	private IChangeReceiver Objectcreator;

	public ExampleStateChangedHandler(IChangeReceiver dtoObject) {
		this.Objectcreator = dtoObject;
		Objectcreator.registerChangeHander(this);
	}

	@Override
	public void sensorDataChanged(DtoSensor updatedDtoSensor) {
		System.out.println("Sensor value changed:");
		System.out.println(updatedDtoSensor);
	}

	@Override
	public void deviceDataChanged(DtoDevice updatedDtoDevice) {
		System.out.println("New device dto  created");
		System.out.println(updatedDtoDevice.getId());
	}

}
