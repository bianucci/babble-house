package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.IChangeReceiver;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class BoStateChangedHandler implements IBoStateChangedHandler {
	private IChangeReceiver Objectcreator;

	public BoStateChangedHandler(IChangeReceiver dtoObject) {
		this.Objectcreator = dtoObject;
		Objectcreator.registerChangeHandler(this);
	}

	@Override
	public void sensorDataChanged(DtoSensor updatedDtoSensor) {
		Notification.show("Sensor value changed\nNewValue: " + updatedDtoSensor.getLatestValue(), Type.TRAY_NOTIFICATION);
		//System.out.println("Sensor value changed:");
		//System.out.println(updatedDtoSensor);
	}

	@Override
	public void deviceDataChanged(DtoDevice updatedDtoDevice) {
		System.out.println("New device dto  created");
		System.out.println(updatedDtoDevice.getId());
	}

}
