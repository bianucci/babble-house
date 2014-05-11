package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.repositories.*;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T>{
	
	// Adds a Dto
	T dto;
		
	// Adds a repository
	IRepositoryBase<T> repository = new RepositoryBase<T>(); 

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
