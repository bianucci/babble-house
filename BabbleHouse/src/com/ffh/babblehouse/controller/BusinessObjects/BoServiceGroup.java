package com.ffh.babblehouse.controller.BusinessObjects;

import java.util.ArrayList;
import java.util.List;

import com.ffh.babblehouse.controller.repositories.ServiceGroupRepository;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;

public class BoServiceGroup extends BoBase<DtoServiceGroup> {

		public BoServiceGroup(){
			this.repository = new ServiceGroupRepository();
		}
	
		public List<DtoServiceGroup> getServiceGrouplist(){
			return ((ServiceGroupRepository)this.repository).getServiceGrouplist();
		}

		public Object[] convertToArray(List<DtoServiceGroup> serviceGroupList){

			List<Object> tempOuter = new ArrayList<Object>();
			
			for(int i=0; i<serviceGroupList.size();i++){
				ArrayList<Object> tempInner = new ArrayList<Object>();
				tempInner.add(serviceGroupList.get(i).getName());
				
				List<DtoSensor> sensorsList = serviceGroupList.get(i).getSensors();
				if(sensorsList.size()>0) {
					tempInner.add("Sensors");
					for(int j=0;j<sensorsList.size();j++)
						tempInner.add(sensorsList.get(j).getSensorName());
				}	
				
				List<DtoDevice> actuatorsList = serviceGroupList.get(i).getDevices();
				if(actuatorsList.size()>0){
					tempInner.add("Actuators");
					for(int j=0;j<actuatorsList.size();j++)
						tempInner.add(actuatorsList.get(j).getDeviceName());
				}
				
				Object[] innerArray = tempInner.toArray();
				
				tempOuter.add(innerArray);
			}
			return tempOuter.toArray();
		}
		
		public DtoServiceGroup getServiceGroupByName(String serviceGroupName){
			return ((ServiceGroupRepository)this.repository).getServiceGroupByName(serviceGroupName);	
		}
}
