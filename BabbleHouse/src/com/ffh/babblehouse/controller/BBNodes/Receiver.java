package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoValue;
import com.ffh.babblehouse.observing.Observer;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Receiver extends Thread implements Subject{
	SerialPort serialPort;
	DtoServiceGroup dtoServiceGroup;
	ServiceGroupQueue newServiceGroupQueue;
	  private List<Observer>  obsrevers;
	 
	
	public Receiver(SerialPort serialPort) {
		this.serialPort = serialPort;
		dtoServiceGroup = new DtoServiceGroup();
	     obsrevers= new ArrayList<Observer > ();
	     newServiceGroupQueue= ServiceGroupQueue.getInstance();
	     

	}
	 public void RegisterObserver(Observer o){
		   //add an observer to be notified

         obsrevers.add(o);
        } 
         public void RemoveTheObserver(Observer o){
      	   //remove an observers from list

             int i= obsrevers.indexOf(o);
             if(i >=0){
             obsrevers.remove(i);
             }
         }
   public void NotifyObserever(Object thisObject){
	   
	   //notify all  observersin the list when a new object is created
for(int i=0; i< obsrevers.size(); i++){
  Observer obsrever= (Observer)obsrevers.get(i);
  if(thisObject instanceof DtoSensor){

  obsrever.updateSensor((DtoSensor)thisObject); 
 
  }
  if(thisObject instanceof DtoDevice){
	  obsrever.updateDtoDevice( (DtoDevice)thisObject);
	 

  }
}
   }

	@SuppressWarnings("unused")
	public void run() {
		while (true) {
			byte[] isRead = new byte[100];

			Service service = null;
			Beacon beacon = null;
			UARTMessage m = null;

			try {
				// read size and unsign it
				int size = serialPort.readBytes(1)[0] & 0xFF;
				if (size >= 0) {
					isRead = serialPort.readBytes(size);
					m = UARTMessage.parseFrom(isRead);

					if (m.getType() == Type.SERVICE) {
						service = m.getService();
						// a service message was received

						// get id of ZigBee device offering the service

						
						

						// get type of service: either actuator or sensor
						ServiceType serviceType = service.getServiceType();
						if (serviceType.equals(ServiceType.ACTUATOR)) {
							// an actuator value was received
							// creat dto device instance
							DtoDevice newDevice = new DtoDevice();
							// set newdevice id from service message
							newDevice.setID(service.getServiceId());
                             // create newdto Value
							DtoValue newDtoValue = new DtoValue();
							// set  newDtoValue from service message 
							newDtoValue.setValue(service.getValue());
							// creat time stamp
							Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
							// set newDtoValue current time stamp 
							newDtoValue.setCurrentTimestamp(currentTimestamp);
							newDevice.setServiceGroupId(service.getServiceGroupId());
							// add the value to dto value list
							newDevice.getValues().add(newDtoValue);
							
							// create an instance on Dto Device
							
							// calling the notifying method

							NotifyObserever(newDevice);
						


						} else if (serviceType.equals(ServiceType.SENSOR)) {
							// a sensor value was received
							
							// create a dto sensor instance
							DtoSensor newDtoSensor = new DtoSensor();
							// set newDtoSensor id from service message value
							newDtoSensor.setId(service.getServiceId());
							// set newDtoSensor measuring units
							newDtoSensor.setMeasuringUnit(service.getInfo());
							
                            // create newDtoValue  instance
							DtoValue newDtoValue = new DtoValue();
							// set newDtoValue value from service mesage
							newDtoValue.setValue(service.getValue());
							
							// create a current time stamp
							Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
							// Set newDtoValue current time stamp
							newDtoValue.setCurrentTimestamp(currentTimestamp);
							//add newDtoValue to newDtoSensor value list
						    newDtoSensor.getValues().add(newDtoValue);
						    newDtoSensor.setServiceGroupId(service.getServiceGroupId());
						
						    NotifyObserever(newDtoSensor);
						    



						}

						if (m.getType() == Type.BEACON) {
							beacon = m.getBeacon();
							// set dtoServiceGroup name from beacon message info
							dtoServiceGroup.setName(beacon.getName());

							// id of the new ZigBee device in our ZigBee network
							// set dtoServiceGroup Id from beacon message info
							dtoServiceGroup.setId(beacon.getServiceGroupId());
							

							// all services that this ZigBee device offers
							List<Service> serviceList = beacon.getServiceList();

							for (Service newService : serviceList) {
								ServiceType t = newService.getServiceType();
								String NewService;
								if (t.equals(ServiceType.ACTUATOR)) {
									// new actuator available in our system
									NewService = t.toString();

									DtoDevice newDevices = new DtoDevice();
									newDevices.setDeviceName(NewService);
									newDevices.setID(newService.getServiceId());

									dtoServiceGroup.getDevices()
											.add(newDevices);
									dtoServiceGroup.setDevices((dtoServiceGroup
											.getDevices()));

								} else if (t.equals(ServiceType.SENSOR)) {
									// new sensor available in out system
									NewService = t.toString();
									DtoSensor newDtoSensor = new DtoSensor();
									newDtoSensor.setSensorName(NewService);
									newDtoSensor.setId(newService
											.getServiceId());

									dtoServiceGroup.getSensors().add(
											newDtoSensor);
									dtoServiceGroup.setDevices((dtoServiceGroup
											.getDevices()));
									

								}
								newServiceGroupQueue.getDtoServiceGroup().add(dtoServiceGroup);
								
							}

						}
					}
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}

		

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//System.out.println(m);
		}
	}
}
