package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.IBBDataBridge;
import com.ffh.babblehouse.controller.repositories.*;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T>{
	
	// Adds a Dto
	T dto;
		
	// Adds a repository
	IRepositoryBase<T> repository = new RepositoryBase<T>(); 

	// Adds a bridge to allow communication between BO layer and BB layer
	IBBDataBridge bbDataBridge;
	
	// Adds basic DB operations
	public T SelectById(Class<T> DtoClass, int Id){
		return repository.selectById(DtoClass,Id);
	}	
	
	// Adds basic DB operations
	public BoBase<T> SaveOrUpdate(T object){
		repository.saveOrUpdate(object);
		return this;
	}

	// Adds basic DB operations
	public BoBase<T> Delete(T object){
		repository.delete(object);
		return this;
	}
}
