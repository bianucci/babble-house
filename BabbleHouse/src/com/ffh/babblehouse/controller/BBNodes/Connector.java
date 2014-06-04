package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.model.DtoGateway;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Connector {
	String PortName;
	private int flag = 1;

	private SerialPort serialPort;

	DtoGateway dtoGateway = new DtoGateway();
	public Connector(){
		PortConnection();
	}

	// setting the
	private void setGatewayConnectionDetails() {
		if (flag == 1) {
		// default setting
			dtoGateway.setBaudrate(38400);
			dtoGateway.setDatabits(8);
			dtoGateway.setParity_none(0);
			dtoGateway.setStopbits(1);
			flag = 2;
		} else {
			// fetch the deata from the database;
			dtoGateway.setBaudrate(38400);
			dtoGateway.setDatabits(8);
			dtoGateway.setParity_none(0);
			dtoGateway.setStopbits(1);
		}

	}

	// return available port
	/** DANGEROUS: CODE FAILS IF TWO OR MORE COMPORTS AVAILABLE **/
	private void SetComPort() {
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			PortName = portNames[i];
		}
	}

	// open com port for connection
	public void PortConnection() {
		setGatewayConnectionDetails();
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


	}

	public SerialPort getserialPort(){
		return serialPort;
	}
}
