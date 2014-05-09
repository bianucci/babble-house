package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.GateWay;
import com.ffh.babblehouse.controller.repositories.*;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T> extends GateWay{
	
	// Adds a Dto
	T dto;
		
	// Adds a repository
	IRepositoryBase<T> repository = new RepositoryBase<T>(); 

	// Adds basic DB operations
	public void Save(T object){
		repository.save(object);
	}
	
	// Adds basic DB operations
	public void Update(T object){
		repository.update(object);
	}

	// Adds basic DB operations
	public void Delete(T object){
		repository.delete(object);
	}
	
}
