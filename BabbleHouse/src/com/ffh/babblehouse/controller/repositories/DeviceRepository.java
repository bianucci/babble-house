package com.ffh.babblehouse.controller.repositories;

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
}
