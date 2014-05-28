package com.ffh.babblehouse.controller.BBNodes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;


public class Creator  implements IMessage{
	IReader iReader;
private String Msg_type;
	private int Zid_sender;
	private int ServiceId;  // either sensor id or device id
	private int payLoad;
	private String SeviceType;
	private List<String> Payload ;

	
	public Creator(IReader iReader){
	this.iReader=iReader;
	Payload = new ArrayList<String>();
		
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
		Msg_type=type.toString();

		// a service message was received
		// this means that previously a service request was send to a ZigBee
		// device and now we just got the service response
		// the service response can either be sensor value or an actuator value
		if (type.equals(Type.SERVICE)) {

			// a service message was received
			Service service = message.getService();
			
			// get id of ZigBee device offering the service
			int serviceGroupId = service.getServiceGroupId();
			Zid_sender=serviceGroupId;
			
			// get type of service: either actuator or sensor
			ServiceType serviceType = service.getServiceType();
			if (serviceType.equals(ServiceType.ACTUATOR)) {
				// an actuator value was received
				payLoad =service.getValue();
				ServiceId=	service.getServiceId();
				
			} else if (serviceType.equals(ServiceType.SENSOR)) {
				// a sensor value was received
				payLoad= service.getValue();
				ServiceId=	service.getServiceId();
				
				
			}
		}

		// a beacon message was received
		// this means that a new ZigBee device entered our ZigBee network and is
		// telling us ALL kind of services it is offering.
		// till now the only two supported service types are acutator and
		// sensor.
		if (type.equals(Type.BEACON)) {

			Beacon beacon = message.getBeacon();

			// id of the new ZigBee device in our ZigBee network
			int serviceGroupId = beacon.getServiceGroupId();
			Zid_sender=serviceGroupId;
			// all services that this ZigBee device offers
			List<Service> serviceList = beacon.getServiceList();
		
			for (Service newService : serviceList) {
				ServiceType t = newService.getServiceType();
				String NewService;
				if (t.equals(ServiceType.ACTUATOR)) {
					// new actuator available in our system
					NewService=t.toString();
					Payload.add(NewService);
					
				} else if (t.equals(ServiceType.SENSOR)) {
					// new sensor available in out system
					NewService=t.toString();
					Payload.add(NewService);
				}
			}
		}
	}
	
	

	public DtoMessage processMessage( ) {
		ExtactMsgFromReadbytes(iReader.readBytes(), iReader.getLength());
		DtoMessage dtoMsg=new DtoMessage(Msg_type,ServiceId,Zid_sender,Payload);
		return dtoMsg;
	}

	
}
