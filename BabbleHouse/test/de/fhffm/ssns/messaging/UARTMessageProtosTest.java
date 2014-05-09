package de.fhffm.ssns.messaging;

import de.fhffm.ssns.messaging.UARTMessageProtos.Service;
import de.fhffm.ssns.messaging.UARTMessageProtos.ServiceGroup;
import de.fhffm.ssns.messaging.UARTMessageProtos.UARTMessage;
import de.fhffm.ssns.messaging.UARTMessageProtos.UARTMessage.Type;

public class UARTMessageProtosTest {

	public static void main(String[] args) {
		
		UARTMessage message = UARTMessageProtos.UARTMessage.getDefaultInstance();
		
		Type type = message.getType();
		
		if(type.equals(Type.SERVICE)){
			// a service message was received
			message.getServices()
		}
		
		if(type.equals(Type.BEACON)){
			// a beacon message was received telling us all services
			message.getBeacon()
		}
		
		ServiceGroup serviceGroup = UARTMessageProtos.ServiceGroup.getDefaultInstance();
		serviceGroup.getServiceList();
		
		
	}

}
