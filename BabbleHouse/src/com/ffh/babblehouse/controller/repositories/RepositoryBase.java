package com.ffh.babblehouse.controller.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RepositoryBase<T> implements IRepositoryBase<T> {
	
	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("babblehousedb"); 
	protected EntityManager em = factory.createEntityManager();  
	
	// Saves data to DB
	public void saveOrUpdate(final T object){
		transact( new Runnable() { 
			public void run(){
				em.persist(object);
			} 
		});
	}
	
	// Deletes data from DB
	public void delete(final T object){
		transact( new Runnable() { 
			public void run(){
				em.remove(object);
			} 
		});
	}
	
	private void transact(Runnable action){
		
		try{
			em.getTransaction().begin(); 
		
			action.run();
	
			em.getTransaction().commit();
		}
		catch(Exception e){
			
		}
	}
}
