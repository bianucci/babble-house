package com.ffh.babblehouse.messaging;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;

public class SendReceiveServiceMessages {

	private static class Sender extends Thread {
		public void run() {
			while (true) {

				Builder serviceBuilder = Service.newBuilder();
				serviceBuilder.setInfo("" + System.currentTimeMillis());
				serviceBuilder.setServiceId(1);
				serviceBuilder.setServiceType(ServiceType.SENSOR);
				serviceBuilder.setServiceGroupId(2);
				Service service = serviceBuilder.build();

				UARTMessage uartMessage = UARTMessage.newBuilder()
						.setType(Type.SERVICE).setService(service).build();

				byte[] message = uartMessage.toByteArray();
				int length = message.length;

				try {
					// send one byte containing the length of the next
					// message
					serialPort.writeByte((byte) length);

					// send the next message
					serialPort.writeBytes(message);

					System.out.println("SENT FOLLOWING PROTOBUF MESSAGE:");
					System.out.println(service.toString());

					Thread.sleep(1000);

				} catch (SerialPortException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

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

				System.err.println("RECEIVED FOLLOWING PROTOBUF MESSAGE:");
				System.err.println(m);
			}
		}

	}

	// static SerialPort serialPort = new SerialPort("COM4");
	static SerialPort serialPort = new SerialPort("COM5");

	public static void main(String[] args) throws SerialPortException,
			InterruptedException, SerialPortTimeoutException {
		serialPort.openPort();
		serialPort.setParams(38400, 8, 1, 0);

		Receiver r = new Receiver();
		Sender s = new Sender();

		r.start();
		s.start();
	}
}