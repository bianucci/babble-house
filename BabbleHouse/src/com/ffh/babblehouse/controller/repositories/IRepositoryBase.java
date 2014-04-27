package com.ffh.babblehouse.controller.repositories;

public interface IRepositoryBase<T> {
	
	// Saves data to DB
	void save(T object);
	
	// Deletes data from DB
	void delete(T object);
	
	//Updates data from DB
	void update(T object);
}
