package com.ffh.babblehouse.controller.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.Query;

import com.ffh.babblehouse.model.DtoUDR;

public class UDRRepository extends RepositoryBase<DtoUDR> {
	@SuppressWarnings("unchecked")
	public List<DtoUDR> getUDRList(){
		
		Callable<Object> transaction = new Callable<Object>() {
			@Override
			public List<DtoUDR> call() throws Exception {
				
				Query query = em.createQuery("SELECT dtoUDR FROM DtoUDR dtoUDR");
				
				return (List<DtoUDR>) query.getResultList();
			}};
		
		return (List<DtoUDR>) this.foreignTransact(transaction);
		
	}
	
	// Removes UDR from DB by id
	public void deleteById(final int itemId) {
		try {
	
			transact( new Runnable() {

				@Override
				public void run() {
					em.createQuery("DELETE from DtoUDR dtoUdr WHERE dtoUdr.id=" + itemId).executeUpdate();
				} 
			});
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
