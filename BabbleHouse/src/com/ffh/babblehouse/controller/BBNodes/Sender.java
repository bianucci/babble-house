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

			System.out.println("SENT FOLLOWING PROTOBUF MESSAGE:");

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
