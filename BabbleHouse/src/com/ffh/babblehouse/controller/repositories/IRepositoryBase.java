package com.ffh.babblehouse.controller.repositories;

public interface IRepositoryBase<T> {
	
	// Saves data to DB
	void saveOrUpdate(T object);
	
	// Deletes data from DB
	void delete(T object);
	
	T selectById(Class<T> thisClass, final int Id);
}
