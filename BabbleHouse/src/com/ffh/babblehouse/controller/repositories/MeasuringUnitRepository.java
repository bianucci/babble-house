package com.ffh.babblehouse.controller.repositories;

import java.util.concurrent.Callable;

import com.ffh.babblehouse.model.DtoMeasuringUnit;

public class MeasuringUnitRepository extends RepositoryBase<DtoMeasuringUnit>{

	public DtoMeasuringUnit selectByUnitName(final String unitName) {
		
		try {
				return transact( new Callable<DtoMeasuringUnit>() {
									@Override
									public DtoMeasuringUnit call() throws Exception {
										return (DtoMeasuringUnit) em.createQuery("SELECT unit FROM DtoMeasuringUnit unit ").getSingleResult();
									} 
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return null;
	}

	
}
