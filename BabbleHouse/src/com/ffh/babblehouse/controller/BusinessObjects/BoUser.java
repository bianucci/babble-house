package com.ffh.babblehouse.controller.BusinessObjects;

import com.ffh.babblehouse.controller.repositories.UserRepository;
import com.ffh.babblehouse.model.DtoUser;

public class BoUser extends BoBase<DtoUser>{

	public void Register(DtoUser newUser){
		this.repository.saveOrUpdate(newUser);
	}
	
	public void Unregister(DtoUser user){
		this.repository.delete(user);
	}
	
	public boolean Validate(DtoUser userBeingVerified){
		UserRepository userRepository = new UserRepository();
		dto = userRepository.selectByUserName(userBeingVerified.getUserName());
		if(dto == null) return false;

		if(userBeingVerified.getPassword().equals(dto.getPassword()))
			return true;
		
		return false;
	}
}
