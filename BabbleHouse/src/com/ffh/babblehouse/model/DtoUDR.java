package com.ffh.babblehouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class DtoUDR{
	@Id 
	@GeneratedValue
	protected int id;
	
	int desired_val;
	
	public int getId() { 
		return id; 
	}
	
	public int getDesired_val() {
		return desired_val;
	}
	public void setDesired_val(int desired_val) {
		this.desired_val = desired_val;
	}
	
	
	

}
