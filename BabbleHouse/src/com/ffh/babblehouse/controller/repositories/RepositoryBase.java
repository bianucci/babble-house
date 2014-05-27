package com.ffh.babblehouse.controller.repositories;

import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RepositoryBase<T> implements IRepositoryBase<T> {
	
	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("babblehousedb"); 
	protected EntityManager em = factory.createEntityManager();  
	
	// Saves data to DB
	public T selectById(Class<T> dtoClass, final int Id){
		
		
		try {
			T class1 = dtoClass.newInstance();
			final Class<? extends Object> class1Class = class1.getClass();
			
			class1 = transact( new Callable<T>() {
				@SuppressWarnings("unchecked")
				@Override
				public T call() throws Exception {
					return (T) em.find(class1Class, Id);
				} 
			});
			
			return class1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
				

		
		return null;
	}
	
	// Saves or Updates data to DB
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
	
	protected void transact(Runnable action){
		
		try{
			em.getTransaction().begin(); 
		
			action.run();
	
			em.getTransaction().commit();
		}
		catch(Exception e){
			showConnectionError(e,"Could not commit transaction. Issue in RepositoryBase.Transact(Runnable action).");
		}
	}
	
	protected T transact(Callable<T> action){
		T t = null;
		try{
			em.getTransaction().begin(); 
		
			t = action.call();
	
			em.getTransaction().commit();
		}
		catch(Exception e){
			showConnectionError(e,"Could not commit transaction. Issue in RepositoryBase.Transact(Callable<T> action).");
		}

		return t;
	}	
	
	private void showConnectionError(Exception e, String messageToShow){
		System.out.println("");
		System.out.println(e.getStackTrace());
		System.out.println(e.getMessage());
	}
}
