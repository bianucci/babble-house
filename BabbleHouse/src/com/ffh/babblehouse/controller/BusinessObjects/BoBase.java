package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.IBBDataBridge;
import com.ffh.babblehouse.controller.BBNodes.IConnector;
import com.ffh.babblehouse.controller.BBNodes.Sender;
import com.ffh.babblehouse.controller.BBNodes.ServiceMsgCreator;
import com.ffh.babblehouse.controller.repositories.IRepositoryBase;
import com.ffh.babblehouse.controller.repositories.RepositoryBase;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T>{
	
	// Adds a Dto
	T dto;
		
	// Adds a repository
	IRepositoryBase<T> repository = new RepositoryBase<T>(); 

	// Adds a bridge to allow communication between BO layer and BB layer
	private IConnector c = IConnector.getInstance();
	private Sender s = new Sender(c.getserialPort());
	IBBDataBridge bbDataBridge = new ServiceMsgCreator(s);
	
	public IBBDataBridge getBBDataBridge() {
		return bbDataBridge;
	}

	// Adds basic DB operations
	public T SelectById(Class<T> DtoClass, int Id){
		return repository.selectById(DtoClass,Id);
	}	
	
	// Adds basic DB operations
	public BoBase<T> SaveOrUpdate(final T object){
		Runnable transaction = new Runnable() {
			
			@Override
			public void run() {
				repository.saveOrUpdate(object);
			}
		};
		
		repository.transact(transaction);		
		
		return this;
	}
	
	// Adds basic DB operations
	public BoBase<T> Delete(T object){
		repository.delete(object);
		return this;
	}
}
