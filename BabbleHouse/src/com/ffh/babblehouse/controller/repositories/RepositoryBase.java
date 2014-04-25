package com.ffh.babblehouse.controller.repositories;

public class RepositoryBase<T> implements IRepositoryBase<T> {
	
	// Saves data to DB
	public void save(T object){
		// Hibernate entries to save object
	}
	
	// Deletes data from DB
	public void delete(T object){
		// Hibernate entries to delete object
	}
	
	//Updates data from DB
	public void update(T object){
		// Hibernate entries to update object
	}
	
}
