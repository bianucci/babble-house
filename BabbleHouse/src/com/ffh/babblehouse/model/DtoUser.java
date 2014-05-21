package com.ffh.babblehouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DtoUser {
	
	@Id 
	@GeneratedValue
	int Id;
	
	String userName, password;

	public int getId(){
		return Id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
