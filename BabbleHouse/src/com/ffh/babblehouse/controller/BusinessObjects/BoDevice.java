package com.ffh.babblehouse.controller.BusinessObjects;

import java.sql.Timestamp;
import java.util.List;

import com.ffh.babblehouse.controller.BBNodes.Connector;
import com.ffh.babblehouse.controller.BBNodes.IBBDataBridge;
import com.ffh.babblehouse.controller.BBNodes.Sender;
import com.ffh.babblehouse.controller.BBNodes.ServiceMsgCreator;
import com.ffh.babblehouse.controller.repositories.DeviceRepository;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoValue;

public class BoDevice extends BoBase<DtoDevice> {

	public BoDevice() {
		this.repository = new DeviceRepository();
	}

	public List<DtoValue> getLastDeviceValuesByName(String deviceName,
			int amountOfRecords) {
		return ((DeviceRepository) this.repository).getLastDeviceValuesByName(
				deviceName, amountOfRecords);
	}

	public DtoDevice getDeviceByName(String deviceName, int amountOfRecords) {
		DtoDevice dtoDevice = ((DeviceRepository) this.repository)
				.getDeviceByName(deviceName, amountOfRecords);
		return dtoDevice == null ? new DtoDevice() : dtoDevice;
	}

	public void addDeviceValue(DtoDevice dtoDevice, int value) {
		DtoValue dtoValue = new DtoValue();
		dtoValue.setValue(value);
		dtoValue.setCurrentTimestamp(new Timestamp(System.currentTimeMillis()));
		dtoDevice.getValues().add(dtoValue);
		
		bbDataBridge.changeDeviceStatus(dtoDevice);
	}

}
