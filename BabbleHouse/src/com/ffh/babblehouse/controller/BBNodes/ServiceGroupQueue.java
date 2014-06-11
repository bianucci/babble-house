package com.ffh.babblehouse.controller.BBNodes;

import java.util.ArrayList;
import java.util.List;

import com.ffh.babblehouse.model.DtoServiceGroup;

public class ServiceGroupQueue {

	private static ServiceGroupQueue instance = null;
	private List<DtoServiceGroup> dtoServiceGroup = new ArrayList<DtoServiceGroup>();
	private List<DtoServiceGroup> dtoServiceGroupbackup = new ArrayList<DtoServiceGroup>();


	public List<DtoServiceGroup> getDtoServiceGroupList() {
		return dtoServiceGroup;
	}

	public static ServiceGroupQueue getInstance() {
		if (instance == null) {
			instance = new ServiceGroupQueue();
		}
		return instance;
	}
	// implementing fifo  
	//first DtoServiceGroup in is first DtoServiceGroup out
	public  DtoServiceGroup getDtoServiceGroup(){
		DtoServiceGroup newdtoServiceGroup = dtoServiceGroup.get(0);
		dtoServiceGroup.remove(0);
		dtoServiceGroupbackup.add(newdtoServiceGroup);
		return newdtoServiceGroup;
		
	}

	
	
	
}
