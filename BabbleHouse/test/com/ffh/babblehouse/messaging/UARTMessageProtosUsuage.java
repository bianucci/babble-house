package com.ffh.babblehouse.messaging;

import java.util.Arrays;
import java.util.List;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;


public class UARTMessageProtosUsuage {

	@SuppressWarnings("unused")
	public void showProtosUsuage(byte[] dataReceived, int lengthOfProto) {
		
		//1. step: read one byte from COM4
		//2. step: convert byte to integer and read value. lets assume it's 142
		//3. step: read 142 bytes from COM4
		//4. step: convert the 142 bytes to one protobuf object
		//5. step: process protobuf object
		//6. step: restart !

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
			int serviceGroupId = service.getServiceGroupId();

			// get type of service: either actuator or sensor
			ServiceType serviceType = service.getServiceType();

			if (serviceType.equals(ServiceType.ACTUATOR)) {
				// an actuator value was received
			} else if (serviceType.equals(ServiceType.SENSOR)) {
				// a sensor value was received
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

			// all services that this ZigBee device offers
			List<Service> serviceList = beacon.getServiceList();

			for (Service newService : serviceList) {
				ServiceType t = newService.getServiceType();
				if (t.equals(ServiceType.ACTUATOR)) {
					// new actuator available in our system
				} else if (t.equals(ServiceType.SENSOR)) {
					// new sensor available in out system
				}
			}
		}
	}
}
