package com.ffh.babblehouse.controller.BBNodes;

import java.util.ArrayList;
import java.util.List;

import com.ffh.babblehouse.model.DtoServiceGroup;

public class ServiceGroupQueue {

	private static ServiceGroupQueue instance = null;
	private List<DtoServiceGroup> dtoServiceGroup = new ArrayList<DtoServiceGroup>();

	public List<DtoServiceGroup> getDtoServiceGroup() {
		return dtoServiceGroup;
	}

	public static ServiceGroupQueue getInstance() {
		if (instance == null) {
			instance = new ServiceGroupQueue();
		}
		return instance;
	}

	
	
}
