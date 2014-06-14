package com.ffh.babblehouse.controller.BusinessObjects;

import java.util.List;

import com.ffh.babblehouse.controller.repositories.UDRRepository;
import com.ffh.babblehouse.model.DtoUDR;

public class BoUDR extends BoBase<DtoUDR> {

	public BoUDR(){
		this.repository = new UDRRepository();
	}
	
	public List<DtoUDR> getUDRList() {
		return ((UDRRepository)this.repository).getUDRList();
	}
	
	public void deleteById(int itemId){
		((UDRRepository) this.repository).deleteById(itemId);
	}
}
