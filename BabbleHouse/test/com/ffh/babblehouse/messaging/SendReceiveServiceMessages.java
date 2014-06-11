package com.ffh.babblehouse.messaging;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;

public class SendReceiveServiceMessages {

	private static class Receiver extends Thread {
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

	 static SerialPort serialPort = new SerialPort("COM15");
	// static SerialPort serialPort = new SerialPort("COM14");

	public static void main(String[] args) throws SerialPortException,
			InterruptedException, SerialPortTimeoutException {

		// serialPort.openPort();
		// serialPort.setParams(38400, 8, 1, 0);

		Receiver r = new Receiver();
		// Sender s = new Sender();

		r.start();
		// s.start();
	}
}