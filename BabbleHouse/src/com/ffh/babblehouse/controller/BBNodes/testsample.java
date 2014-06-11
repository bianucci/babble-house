package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class testsample {

	public static void main(String[] args) throws SerialPortException,
			InterruptedException, SerialPortTimeoutException {

		Connector newConnector = new Connector();
//		SerialPort serialPort = new SerialPort("COM14");
//		serialPort.openPort();
//		serialPort.setParams(38400, 8, 1, 0);

		SerialPort serialPort = newConnector.getserialPort();
		
		 //Sender newSender = new Sender(serialPort);
	//	newSender.SenderMessage(1222, 34);
		/*
		 * Receiver reader = new Receiver(); reader.serialPort.openPort();
		 * reader.start(); reader.serialPort.setParams(38400, 8, 1, 0);
		 */

		Receiver newReceiver = new Receiver(
				serialPort);
		newReceiver.start();
	}

}
