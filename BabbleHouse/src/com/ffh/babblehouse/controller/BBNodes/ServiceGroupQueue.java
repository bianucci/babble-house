package com.ffh.babblehouse.controller.BBNodes;

import java.util.ArrayList;
import java.util.List;

import com.ffh.babblehouse.model.DtoServiceGroup;

public class ServiceGroupQueue {

	private static ServiceGroupQueue instance = null;
	private List<DtoServiceGroup> dtoServiceGroup = new ArrayList<DtoServiceGroup>();

	public static ServiceGroupQueue getInstance() {
		if (instance == null) {
			instance = new ServiceGroupQueue();
		}
		return instance;
	}

	public DtoServiceGroup getFirstGroupFromQueue() {
		DtoServiceGroup sg = dtoServiceGroup.get(0);
		if (sg != null) {
			dtoServiceGroup.remove(sg);
			return sg;
		} else {
			return null;
		}
	}

	public List<DtoServiceGroup> getDtoServiceGroup() {
		ArrayList<DtoServiceGroup> toReturn = new ArrayList<DtoServiceGroup>(this.dtoServiceGroup);
		this.dtoServiceGroup = new ArrayList<DtoServiceGroup>();
		return toReturn;
	}
	
}
