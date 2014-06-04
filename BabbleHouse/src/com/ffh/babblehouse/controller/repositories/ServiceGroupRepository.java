package com.ffh.babblehouse.controller.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.Query;
import com.ffh.babblehouse.model.DtoServiceGroup;

public class ServiceGroupRepository extends RepositoryBase<DtoServiceGroup>{

	@SuppressWarnings("unchecked")
	public List<DtoServiceGroup> getServiceGrouplist() {
		
		Callable<Object> transaction = new Callable<Object>() {
			@Override
			public List<DtoServiceGroup> call() throws Exception {
				
				Query query = em.createQuery("SELECT dtoServiceGroup FROM DtoServiceGroup dtoServiceGroup");
				
				//query.setMaxResults(amountOfRecords);
				return (List<DtoServiceGroup>) query.getResultList();
			}};
		
		return (List<DtoServiceGroup>) this.foreignTransact(transaction);
	}

	public DtoServiceGroup getServiceGroupByName(final String serviceGroupName) {
		Callable<DtoServiceGroup> transaction = new Callable<DtoServiceGroup>() {
			@Override
			public DtoServiceGroup call() throws Exception {
				
				Query query = em.createQuery("SELECT dtoServiceGroup FROM DtoServiceGroup dtoServiceGroup WHERE dtoServiceGroup.name='"+ String.valueOf(serviceGroupName) +"'");
				
				//query.setMaxResults(amountOfRecords);
				return (DtoServiceGroup) query.getSingleResult();
			}};

			DtoServiceGroup dtoServiceGroup;
		try {
			dtoServiceGroup = this.transact(transaction);
		}catch(Exception e){
			dtoServiceGroup = null;
		}
			return dtoServiceGroup;
	}
	
	
	
}
