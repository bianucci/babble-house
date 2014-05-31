package com.ffh.babblehouse.controller.BBNodes;


import java.util.Arrays;
import java.util.List;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoValue;
import com.google.protobuf.InvalidProtocolBufferException;
import java.sql.*;

public class ProtoMessageProcessor implements IdtoServiceGroup{
	IReader iReader;
	DtoServiceGroup dtoServiceGroup= new DtoServiceGroup();	
	

	
	public ProtoMessageProcessor(IReader iReader){
	this.iReader=iReader;
	
		
	}
	
	
	public void ExtactMsgFromReadbytes(byte[] dataReceived, int lengthOfProto) {

		byte[] serialProtoBuf = Arrays.copyOf(dataReceived, lengthOfProto);

		UARTMessage message = null;
		try {
			message = UARTMessageProtos.UARTMessage.parseFrom(serialProtoBuf);
		} catch (InvalidProtocolBufferException e) {
			// protocol buffer message could not be deserialized
			e.printStackTrace();
		}

		Type type = message.getType();
		

		// a service message was received
		// this means that previously a service request was send to a ZigBee
		// device and now we just got the service response
		// the service response can either be sensor value or an actuator value
		if (type.equals(Type.SERVICE)) {

			// a service message was received
			Service service = message.getService();
			
			// get id of ZigBee device offering the service
			
			dtoServiceGroup.setId(service.getServiceGroupId());
			
			// get type of service: either actuator or sensor
			ServiceType serviceType = service.getServiceType();
			if (serviceType.equals(ServiceType.ACTUATOR)) {
				// an actuator value was received
				DtoDevice newDevices= new DtoDevice();
				newDevices.setID(service.getServiceId());
				
				DtoValue newDtoValue= new DtoValue();
				newDtoValue.setValue(service.getValue());
			
				 Timestamp  currentTimestamp= new Timestamp(System.currentTimeMillis());
				newDtoValue.setCurrentTimestamp(currentTimestamp);
				newDevices.getValues().add(newDtoValue);
				
				dtoServiceGroup.getDevices().add(newDevices);
				dtoServiceGroup.setDevices((dtoServiceGroup.getDevices()));
				
				
			} else if (serviceType.equals(ServiceType.SENSOR)) {
				// a sensor value was received
				DtoSensor newDtoSensor= new DtoSensor();
				newDtoSensor.setId(service.getServiceId());
				
				DtoValue newDtoValue= new DtoValue();
				newDtoValue.setValue(service.getValue());
			
				 Timestamp  currentTimestamp= new Timestamp(System.currentTimeMillis());
				newDtoValue.setCurrentTimestamp(currentTimestamp);
				newDtoSensor.getValues().add(newDtoValue);
				dtoServiceGroup.getSensors().add(newDtoSensor);
				dtoServiceGroup.setDevices((dtoServiceGroup.getDevices()));
			
			}
			
		}

		// a beacon message was received
		// this means that a new ZigBee device entered our ZigBee network and is
		// telling us ALL kind of services it is offering.
		// till now the only two supported service types are acutator and
		// sensor.
		if (type.equals(Type.BEACON)) {

			Beacon beacon = message.getBeacon();
			dtoServiceGroup.setName(beacon.getName());

			// id of the new ZigBee device in our ZigBee network
			dtoServiceGroup.setId(beacon.getServiceGroupId());
			dtoServiceGroup.setName(beacon.getName());
		
			// all services that this ZigBee device offers
			List<Service> serviceList = beacon.getServiceList();
		
			for (Service newService : serviceList) {
				ServiceType t = newService.getServiceType();
				String NewService;
				if (t.equals(ServiceType.ACTUATOR)) {
					// new actuator available in our system
					NewService=t.toString();

					DtoDevice newDevices= new DtoDevice();
					newDevices.setDeviceName(NewService);
					newDevices.setID(newService.getServiceId());
					
					dtoServiceGroup.getDevices().add(newDevices);
					dtoServiceGroup.setDevices((dtoServiceGroup.getDevices()));
					
				} else if (t.equals(ServiceType.SENSOR)) {
					// new sensor available in out system
					NewService=t.toString();
					DtoSensor newDtoSensor=new DtoSensor();
					newDtoSensor.setSensorName(NewService);
					newDtoSensor.setId(newService.getServiceId());

					
					dtoServiceGroup.getSensors().add(newDtoSensor);
					dtoServiceGroup.setDevices((dtoServiceGroup.getDevices()));
					
					
				}
			}
		}
	}

	@Override
	public DtoServiceGroup getServiceGroupdtoObject() {
		// TODO Auto-generated method stub
		ExtactMsgFromReadbytes(iReader.readMessageBytes(), iReader.getLength());

		return dtoServiceGroup;
	}

	
}
