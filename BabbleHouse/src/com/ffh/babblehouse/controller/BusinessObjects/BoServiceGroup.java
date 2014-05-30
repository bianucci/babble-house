package com.ffh.babblehouse.controller.BusinessObjects;

import java.util.ArrayList;
import java.util.List;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;

public class BoServiceGroup extends BoBase<DtoServiceGroup> {

	// TODO Get all ServiceGroups from DB and return an arrayList of them
		public List<DtoServiceGroup> getServiceGrouplist(){

			// Manually filling array list (Should be requested from business layer)
			List<DtoServiceGroup> dtoServiceGroupList = new ArrayList<DtoServiceGroup>();

				DtoServiceGroup dtoServiceGroup = new DtoServiceGroup();
				dtoServiceGroup.setName("Service Group 1");
			
				List<DtoSensor> dtoSensorList = new ArrayList<DtoSensor>();
				
					DtoSensor dtoSensor1 = new DtoSensor();
					dtoSensor1.setSensorName("Sensor1");
					DtoSensor dtoSensor2 = new DtoSensor();
					dtoSensor2.setSensorName("Sensor2");
			
					dtoSensorList.add(dtoSensor1);
					dtoSensorList.add(dtoSensor2);
				
				List<DtoDevice> dtoDeviceList = new ArrayList<DtoDevice>();
									
					DtoDevice dtoDevice1 = new DtoDevice();
					dtoDevice1.setDeviceName("Actuator1");
					DtoDevice dtoDevice2 = new DtoDevice();
					dtoDevice2.setDeviceName("Actuator2");
					
					dtoDeviceList.add(dtoDevice1);
					dtoDeviceList.add(dtoDevice2);
						
				dtoServiceGroup.setSensors(dtoSensorList);
				dtoServiceGroup.setDevices(dtoDeviceList);
				
			dtoServiceGroupList.add(dtoServiceGroup);
			
			// test
			
			DtoServiceGroup dtoServiceGroup2 = new DtoServiceGroup(); 
			dtoServiceGroup2.setName("Service Group 2");
			
			List<DtoSensor> dtoSensorList2 = new ArrayList<DtoSensor>();
			
				DtoSensor dtoSensor3 = new DtoSensor();
				dtoSensor3.setSensorName("Sensor3");
				DtoSensor dtoSensor4 = new DtoSensor();
				dtoSensor4.setSensorName("Sensor4");
			
			dtoSensorList2.add(dtoSensor3);
			dtoSensorList2.add(dtoSensor4);
			
			List<DtoDevice> dtoDeviceList2 = new ArrayList<DtoDevice>();
			
				DtoDevice dtoDevice3 = new DtoDevice();
				dtoDevice3.setDeviceName("Actuator3");
				DtoDevice dtoDevice4 = new DtoDevice();
				dtoDevice4.setDeviceName("Actuator4");
			
				dtoDeviceList2.add(dtoDevice3);
				dtoDeviceList2.add(dtoDevice4);
					
			dtoServiceGroup2.setSensors(dtoSensorList2);
			dtoServiceGroup2.setDevices(dtoDeviceList2);
			
			dtoServiceGroupList.add(dtoServiceGroup2);
					
			return dtoServiceGroupList;
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
}
