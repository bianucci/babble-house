package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.repositories.MeasuringUnitRepository;
import com.ffh.babblehouse.model.DtoMeasuringUnit;

public class BoMeasuringUnit extends BoBase<DtoMeasuringUnit>{

	public BoMeasuringUnit(){
		this.repository = new MeasuringUnitRepository();
	}
	
	// Adds basic DB operations
	public DtoMeasuringUnit SelectByUnitName(String unitName){
		return ((MeasuringUnitRepository)repository).selectByUnitName(unitName);
	}	
	
}
