package com.ffh.babblehouse.controller.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.Query;

import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoValue;

public class SensorRepository extends RepositoryBase<DtoSensor>{
	
	@SuppressWarnings("unchecked")
	public List<DtoValue> getLastSensorValuesById(final int sensorId, final int amountOfRecords){

		Callable<Object> transaction = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				
				Query query = em.createQuery("SELECT values FROM DtoSensor sensor WHERE sensor.id='"+ String.valueOf(sensorId) +"'");
				
				query.setMaxResults(amountOfRecords);
				return query.getResultList();
			}};

			return (List<DtoValue>) this.foreignTransact(transaction);
	}
	
	@SuppressWarnings("unchecked")
	public List<DtoValue> getLastSensorValuesByName(final String sensorName, final int amountOfRecords){

		Callable<Object> transaction = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				
				Query query = em.createQuery("SELECT values FROM DtoSensor sensor WHERE sensor.SensorName='"+ sensorName +"'");
				
				query.setMaxResults(amountOfRecords);
				return query.getResultList();
			}};

			return (List<DtoValue>) this.foreignTransact(transaction);
	}
	
}
