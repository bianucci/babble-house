package com.ffh.babblehouse.controller.BBNodes;

import java.sql.Timestamp;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;

public class Sender implements IServiceExtractor {
	boolean status;
	SerialPort serialPort;
	DtoDevice dtoDevice=null;
	int serviceGroupId;
	ServiceType serviceType;
	int serviceId;
	int Serviceid;
	Timestamp t;
	int val=0;
	
	public Sender(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public boolean SenderMessage( ) {
	
		Builder serviceBuilder = Service.newBuilder();
		serviceBuilder.setInfo("" + System.currentTimeMillis());
		serviceBuilder.setServiceId(serviceGroupId);
		serviceBuilder.setServiceType(serviceType);
		serviceBuilder.setServiceGroupId(serviceId);
		Service service = serviceBuilder.build();

		UARTMessage uartMessage = UARTMessage.newBuilder()
				.setType(Type.SERVICE).setService(service).build();

		byte[] message = uartMessage.toByteArray();
		int length = message.length;

		try {
			// send one byte containing the length of the next
			// message
			serialPort.writeByte((byte) length);

			// send the next message
			status = serialPort.writeBytes(message);

			System.out.println("SENT FOLLOWING PROTOBUF MESSAGE:");
			System.out.println(service.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SerialPortException e) {
			e.printStackTrace();
		}

		return status;
	}

	@Override
	public void extractservicemessage(DtoSensor sensor) {
		this.serviceId= sensor.getId();
		this.serviceType=serviceType.SENSOR;
		SenderMessage( );
	}

	@Override
	public void extractserviceMessage(DtoDevice devices) {
		this.serviceId= devices.getId();
		this.serviceType=serviceType.ACTUATOR;
		SenderMessage( );
	}

	

	
}
