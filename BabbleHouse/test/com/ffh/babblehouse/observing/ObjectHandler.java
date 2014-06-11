package com.ffh.babblehouse.observing;

import com.ffh.babblehouse.controller.BBNodes.IObseverable;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;

public class ObjectHandler implements Observer{
	   private IObseverable Objectcreator;
	   private DtoDevice dtoDevice;
	   private DtoSensor dtoSensor;
	   public ObjectHandler (IObseverable dtoObject){
		   
		      this.Objectcreator= dtoObject;
		      Objectcreator.RegisterObserver(this);
		   }
	  
	@Override
	public void updateDtoDevice(DtoDevice toDevice) {
		// TODO Auto-generated method stub
		dtoDevice=toDevice;
		displyDevice();
		
	}
	@Override
	public void updateSensor(DtoSensor toSensor) {
		// TODO Auto-generated method stub
		dtoSensor=toSensor;	
		disply1();
	}
	private void disply1 (){
		System.out.println("New sensor dto  created");
	System.out.println(dtoSensor.getMeasuringUnit());
	System.out.println(dtoSensor.getId());
	

	}

	public void displyDevice(){
		System.out.println("New device dto  created");

		System.out.println(dtoDevice.getId());
	
	}
	public DtoDevice getDtoDevice() {
		return dtoDevice;
	}

	public DtoSensor getDtoSensor() {
		return dtoSensor;
	}

}
