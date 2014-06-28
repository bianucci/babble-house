package com.ffh.babblehouse.controller.BBNodes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.controller.BusinessObjects.BoStateChangedHandler;
import com.ffh.babblehouse.controller.BusinessObjects.IBoStateChangedHandler;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoMeasuringUnit;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.ffh.babblehouse.model.DtoValue;
import com.google.protobuf.InvalidProtocolBufferException;

public class Receiver extends Thread implements IChangeReceiver {
	SerialPort serialPort;
	ServiceGroupQueue newServiceGroupQueue;
	IBoStateChangedHandler changeHandler = new BoStateChangedHandler(this);

	public Receiver(SerialPort serialPort) {
		this.serialPort = serialPort;
		newServiceGroupQueue = ServiceGroupQueue.getInstance();
	}

	public void run() {
		while (true) {
			receive();
		}
	}

	public void receive() {
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

						// create newdto Value
						DtoValue newDtoValue = new DtoValue();
						// set newDtoValue from service message
						newDtoValue.setValue(service.getValue());
						// creat time stamp
						Timestamp currentTimestamp = new Timestamp(
								System.currentTimeMillis());
						// set newDtoValue current time stamp
						newDtoValue.setCurrentTimestamp(currentTimestamp);

						// add the value to dto value list
						newDevice.getValues().add(newDtoValue);

						// create an instance on Dto Device

						// calling the notifying method

						valueChanged(newDevice);

					} else if (serviceType.equals(ServiceType.SENSOR)) {
						// a sensor value was received

						// create a dto sensor instance
						DtoSensor newDtoSensor = new DtoSensor();
						newDtoSensor.setValues(new ArrayList<DtoValue>());
						// set newDtoSensor id from service message value
						newDtoSensor.setId(service.getServiceId());
						// set newDtoSensor measuring units
						DtoMeasuringUnit newmeasuringUnit = new DtoMeasuringUnit();

						DtoMeasuringUnit u = UnitUtil.getUnitForInfot(service
								.getInfo());
						newmeasuringUnit.setUnit_name(service.getInfo());
						newDtoSensor.setMeasuringUnit(newmeasuringUnit);

						// create a dummy service dtoservice group
						DtoServiceGroup newdummyDtoServiceGroup = new DtoServiceGroup();
						// set dtoservicegroup id
						newdummyDtoServiceGroup.setId(service
								.getServiceGroupId());
						// setDtoServiceGroup
						newDtoSensor
								.setDtoServiceGroup(newdummyDtoServiceGroup);
						// create newDtoValue instance
						DtoValue newDtoValue = new DtoValue();
						// set newDtoValue value from service mesage
						newDtoValue.setValue(service.getValue());
						newDtoValue.setDtoSensor(newDtoSensor);

						// create a current time stamp
						Timestamp currentTimestamp = new Timestamp(
								System.currentTimeMillis());
						// Set newDtoValue current time stamp
						newDtoValue.setCurrentTimestamp(currentTimestamp);
						// add newDtoValue to newDtoSensor value list
						newDtoSensor.getValues().add(newDtoValue);

						newDtoSensor
								.setUserDefineRules(new ArrayList<DtoUDR>());
						valueChanged(newDtoSensor);

					}
				} else if (m.getType() == Type.BEACON) {
					DtoServiceGroup dtoServiceGroup = new DtoServiceGroup();

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
							newDevices.setId(newService.getServiceId());
							newDevices
									.setUserDefineRules(new ArrayList<DtoUDR>());
							newDevices.setValues(new ArrayList<DtoValue>());

							dtoServiceGroup.getDevices().add(newDevices);
							dtoServiceGroup.setDevices((dtoServiceGroup
									.getDevices()));

							newDevices.setDtoServiceGroup(dtoServiceGroup);

						} else if (t.equals(ServiceType.SENSOR)) {
							// new sensor available in out system
							NewService = t.toString();
							DtoSensor newDtoSensor = new DtoSensor();
							newDtoSensor.setSensorName(NewService);
							newDtoSensor.setId(newService.getServiceId());

							dtoServiceGroup.getSensors().add(newDtoSensor);
							dtoServiceGroup.setDevices((dtoServiceGroup
									.getDevices()));
							newDtoSensor.setDtoServiceGroup(dtoServiceGroup);
							newDtoSensor
									.setUserDefineRules(new ArrayList<DtoUDR>());
							newDtoSensor.setValues(new ArrayList<DtoValue>());
						}
					}
					newServiceGroupQueue.getDtoServiceGroupList().add(
							dtoServiceGroup);
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

		// System.out.println(m);
	}

	@Override
	public void registerChangeHandler(IBoStateChangedHandler o) {
		this.changeHandler = o;

	}

	@Override
	public void valueChanged(Object o) {
		if (o instanceof DtoSensor) {
			changeHandler.sensorDataChanged((DtoSensor) o);
		}
		if (o instanceof DtoDevice) {
			changeHandler.deviceDataChanged((DtoDevice) o);
		}
	}

	public IBoStateChangedHandler getChangeHandler() {
		return changeHandler;
	}

}
