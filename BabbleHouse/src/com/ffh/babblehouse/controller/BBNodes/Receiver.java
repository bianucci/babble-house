package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Receiver extends Thread {
	static SerialPort serialPort = new SerialPort("COM14");

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

					if (m.getType() == Type.SERVICE)
						service = m.getService();
					if (m.getType() == Type.BEACON)
						beacon = m.getBeacon();
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}

			System.err.println("FOLLOWING PROTOBUF MESSAGE:");
			System.err.println(m);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
