package com.ffh.babblehouse.model;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DtoProduct{ 
	@Id 
	@GeneratedValue
	protected int id;
	private String nome; 
	private double preco;  
		
	public int getId() { 
		return id; 
	}
	
	public String getNome() { 
		return nome; 
	} 
	
	public void setNome(String nome) { 
		this.nome = nome; 
	} 
	
	public double getPreco() { 
		return preco; 
	} 
	
	public void setPreco(double preco) { 
		this.preco = preco; 
	} 
}