package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;

public class MesssageSender extends Thread implements IMessageSender {
	boolean status;
	SerialPort serialPort;

	public MesssageSender(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public boolean SenderMessage(int serviceGroupId, int serviceId) {
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
			status = serialPort.writeBytes(message);

			System.out.println("SENT FOLLOWING PROTOBUF MESSAGE:");
			System.out.println(service.toString());
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

	public void run() {
		SenderMessage(2334, 45555);

	}

}
