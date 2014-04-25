package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.BBNodes.BBNode;
import com.ffh.babblehouse.controller.repositories.*;

// Extends BBNode to communicate with sensors and actuators
public abstract class BoBase<T> extends BBNode{
	
	// Adds a Dto
	T dto;
		
	// Adds a repository
	IRepositoryBase<T> repository = new RepositoryBase<T>(); 

}
