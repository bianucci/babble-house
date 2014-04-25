package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.model.DtoLed;
import com.ffh.babblehouse.model.PinStatus;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class BoLed extends BoBase<DtoLed>{

	public BoLed(){
		dto = new DtoLed();
	}

	//TODO Remove this TEST method
	public Component getDtoId(){
		return new Label("Id: " + dto.getId());
	}
	
	//TODO Remove this TEST method
	public Component getDtoPinStatus(){
		return new Label("PinStatus: " + dto.getPinStatus());
	}
	
	//TODO Remove this TEST method
	public void setDtoId(int id){
		dto.setId(id);
	}
	
	//TODO Remove this TEST method
	public void setDtoPinStatus(PinStatus pinStatus){
		dto.setPinStatus(pinStatus);
	}
	
	//TODO Remove this TEST method
	public void toggleDtoPinStatus(){
		if(dto.getPinStatus() == PinStatus.LOW)
			dto.setPinStatus(PinStatus.HIGH);
		else if(dto.getPinStatus() == PinStatus.HIGH)
			dto.setPinStatus(PinStatus.LOW);
	}
	
	//TODO Remove this TEST method
	public void SaveToDB(DtoLed dtoLed){
		repository.save(dtoLed);
	}
	
	//TODO Remove this TEST method
	public void UpdateDB(DtoLed dtoLed){
		repository.update(dtoLed);
	}
	
	//TODO Remove this TEST method
	public void DeleteFromDB(DtoLed dtoLed){
		repository.delete(dtoLed);
	}
}
