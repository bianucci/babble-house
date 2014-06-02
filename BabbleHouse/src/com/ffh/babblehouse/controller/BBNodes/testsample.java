package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class testsample {

	public static void main(String[] args) throws SerialPortException,
			InterruptedException, SerialPortTimeoutException {

		// Connector newConnector = new Connector();
		//SerialPort serialPort = newConnector.PortConnection();
		
		// MesssageSender newSender = new MesssageSender(serialPort);
		// newSender.start();

		 Receiver reader = new Receiver();
		 reader.serialPort.openPort();
		 reader.serialPort.setParams(38400, 8, 1, 0);
		 reader.start();
		//ProtoMessageProcessor newReceiver = new ProtoMessageProcessor(	serialPort);
		//newReceiver.start();
	}

}
