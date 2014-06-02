package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.model.DtoGateway;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Connector implements Iconnector {
	String PortName;
	private int flag = 1;

	private SerialPort serialPort;

	DtoGateway dtoGateway = new DtoGateway();

	// setting the
	private void setGatewayConnectionDetails() {
		if (flag == 1) {
			dtoGateway.setBaudrate(38400);
			dtoGateway.setDatabits(8);
			dtoGateway.setParity_none(1);
			dtoGateway.setStopbits(0);
			flag = 2;
		} else {
			// fetch the deata from the database;
			dtoGateway.setBaudrate(38400);
			dtoGateway.setDatabits(8);
			dtoGateway.setParity_none(1);
			dtoGateway.setStopbits(0);
		}

	}

	// return available port
	private void SetComPort() {
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			PortName = portNames[i];
		}
	}

	// open com port for connection
	public SerialPort PortConnection() {
		setGatewayConnectionDetails();
		
		/** THIS FUNCTION CALL ACTUALLY HAS NO EFFECT AT ALL**/
		SetComPort(); // set availble port
		serialPort = new SerialPort(PortName);
		try {
			serialPort.openPort();
			serialPort.setParams(dtoGateway.getBaudrate(),
					dtoGateway.getDatabits(), dtoGateway.getStopbits(),
					dtoGateway.getParity_none());
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

		return serialPort;

	}

}
