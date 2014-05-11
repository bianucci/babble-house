package com.ffh.babblehouse.model;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DtoProduct{ 
	@Id 
	@GeneratedValue
	protected int id;
	private String name; 
	private double price;  
		
	public int getId() { 
		return id; 
	}
	
	public String getName() { 
		return name; 
	} 
	
	public void setName(String nome) { 
		this.name = nome; 
	} 
	
	public double getPrice() { 
		return price; 
	} 
	
	public void setPrice(double preco) { 
		this.price = preco; 
	} 
}