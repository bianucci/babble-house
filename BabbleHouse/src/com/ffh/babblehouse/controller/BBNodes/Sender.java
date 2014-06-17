package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;

public class Sender implements Isender {
	boolean status;
	SerialPort serialPort;

	
	
	
	public Sender(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public boolean SenderMessage(UARTMessage uartMessage ) {
	
		

		byte[] message = uartMessage.toByteArray();
		int length = message.length;

		try {
			// send one byte containing the length of the next
			// message
			serialPort.writeByte((byte) length);

			// send the next message
			status = serialPort.writeBytes(message);

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

	

	

	
}
