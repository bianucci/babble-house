package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;

public class Sender implements ISender {
	SerialPort serialPort;

	public Sender(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public boolean sendMessage(UARTMessage uartMessage)
			throws SerialPortException {

		boolean status = false;

		byte[] message = uartMessage.toByteArray();
		int length = message.length;

		try {
			// send one byte containing the length of the next
			// message
			boolean writeByte = serialPort.writeByte((byte) length);

			// send the next message
			boolean writeBytes = serialPort.writeBytes(message);

			status = writeByte && writeBytes;

		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return status;
	}
}
