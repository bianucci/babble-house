package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.BBNode;
import com.ffh.babblehouse.controller.repositories.*;
import com.ffh.babblehouse.model.DtoLed;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T> extends BBNode{
	
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
