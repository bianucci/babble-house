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
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.List;

import java.sql.*;

class Receiver extends Thread {
	SerialPort serialPort;
	DtoServiceGroup dtoServiceGroup = new DtoServiceGroup();
	ServiceGroupQueue newServiceGroupQueue= ServiceGroupQueue.getInstance();
	
	public Receiver(SerialPort serialPort) {
		this.serialPort = serialPort;
		dtoServiceGroup = new DtoServiceGroup();
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

						dtoServiceGroup.setId(service.getServiceGroupId());
						

						// get type of service: either actuator or sensor
						ServiceType serviceType = service.getServiceType();
						if (serviceType.equals(ServiceType.ACTUATOR)) {
							// an actuator value was received
							DtoDevice newDevices = new DtoDevice();
							newDevices.setID(service.getServiceId());

							DtoValue newDtoValue = new DtoValue();
							newDtoValue.setValue(service.getValue());
							
							Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
							newDtoValue.setCurrentTimestamp(currentTimestamp);
							newDevices.getValues().add(newDtoValue);

							dtoServiceGroup.getDevices().add(newDevices);
							

						} else if (serviceType.equals(ServiceType.SENSOR)) {
							// a sensor value was received
							DtoSensor newDtoSensor = new DtoSensor();
							newDtoSensor.setId(service.getServiceId());
							newDtoSensor.setMeasuringUnit(service.getInfo());
							

							DtoValue newDtoValue = new DtoValue();
							
							newDtoValue.setValue(service.getValue());
							
							Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
							newDtoValue.setCurrentTimestamp(currentTimestamp);
							
						    newDtoSensor.getValues().add(newDtoValue);
							
							dtoServiceGroup.getSensors().add(newDtoSensor);
					

						}

						if (m.getType() == Type.BEACON) {
							beacon = m.getBeacon();
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

		System.out.println(m);
		}
	}
}
