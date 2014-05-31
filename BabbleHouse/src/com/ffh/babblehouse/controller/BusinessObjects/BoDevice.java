package com.ffh.babblehouse.controller.BusinessObjects;

import java.util.List;

import com.ffh.babblehouse.controller.repositories.DeviceRepository;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoValue;

public class BoDevice extends BoBase<DtoDevice>{

	public BoDevice(){
		this.repository = new DeviceRepository();
	}
	
	public List<DtoValue> getLastDeviceValuesByName(String deviceName, int amountOfRecords){
		return ((DeviceRepository) this.repository).getLastDeviceValuesByName(deviceName, amountOfRecords);
	}
	
}
