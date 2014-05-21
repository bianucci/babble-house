package com.ffh.babblehouse.controller.repositories;

import java.util.concurrent.Callable;

import javax.persistence.Query;

import com.ffh.babblehouse.model.DtoUser;

public class UserRepository extends RepositoryBase<DtoUser> implements IRepositoryBase<DtoUser>{
	// Here go specific queries. Basic queries as Save, Update, and delete are inherited from RepositoryBase
	
	public DtoUser selectByUserName(final String userName){
		Callable<DtoUser> transaction = new Callable<DtoUser>() {

			@Override
			public DtoUser call() throws Exception {
				Query query = em.createQuery("SELECT user FROM DtoUser user WHERE user.userName='"+ userName +"'");
				query.setMaxResults(1);
				return (DtoUser) query.getSingleResult();
			}};

			return this.transact(transaction);
	}
	
}
