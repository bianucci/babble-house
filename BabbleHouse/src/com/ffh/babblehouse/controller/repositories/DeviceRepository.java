package com.ffh.babblehouse.controller.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.Query;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoValue;

public class DeviceRepository extends RepositoryBase<DtoDevice> {

	@SuppressWarnings("unchecked")
	public List<DtoValue> getLastDeviceValuesByName(final String deviceName, final int amountOfRecords){

		Callable<Object> transaction = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				
				Query query = em.createQuery("SELECT values FROM DtoDevice device WHERE device.deviceName='"+ deviceName +"'");
				
				query.setMaxResults(amountOfRecords);
				return query.getResultList();
			}};

			return (List<DtoValue>) this.foreignTransact(transaction);
	}

	public DtoDevice getDeviceByName(final String deviceName,final int amountOfRecords) {
		Callable<DtoDevice> transaction = new Callable<DtoDevice>() {
			@Override
			public DtoDevice call() throws Exception {
				
				Query query = em.createQuery("SELECT device FROM DtoDevice device WHERE device.deviceName='"+ String.valueOf(deviceName) +"'");
				
				query.setMaxResults(amountOfRecords);
				return (DtoDevice) query.getSingleResult();
			}};

			return this.transact(transaction);
	}
	
	public DeviceRepository addDeviceValue(final DtoDevice dtoDevice, final int value){
		
		Runnable transaction = new Runnable() {
			@Override
			public void run() {
				
				DtoValue dtoValue = new DtoValue();
				dtoValue.setValue(value);
				dtoValue.setCurrentTimestamp(new Timestamp(System.currentTimeMillis()));
				dtoValue.setDtoDevice(dtoDevice);
				
				dtoDevice.getValues().add(dtoValue);
								
				saveOrUpdate(dtoDevice);
			}};
			
		this.transact(transaction);

		return this;
	}
}
