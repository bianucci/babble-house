package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;

public class DummyConnector extends IConnector {

	@Override
	public SerialPort getserialPort() {
		final Builder serviceBuilder = Service.newBuilder();
		serviceBuilder.setInfo("Degree Celcius");
		serviceBuilder.setServiceId(1);
		serviceBuilder.setServiceType(ServiceType.SENSOR);
		serviceBuilder.setServiceGroupId(2);
		final Service service = serviceBuilder.build();

		SerialPort s = new SerialPort("") {

			private int tempMax;

			@Override
			public boolean openPort() throws SerialPortException {
				return true;
			}

			@Override
			public boolean setParams(int baudRate, int dataBits, int stopBits,
					int parity) throws SerialPortException {
				return true;
			}

			@Override
			public byte[] readBytes(int byteCount) throws SerialPortException {

				serviceBuilder.setValue((int) (Math.random() * tempMax));
				UARTMessage uartMessage = UARTMessage.newBuilder()
						.setType(Type.SERVICE).setService(service).build();
				if (byteCount == 1) {
					return new byte[] { (byte) uartMessage.getSerializedSize() };
				} else {
					return uartMessage.toByteArray();
				}
			}

			@Override
			public boolean writeBytes(byte[] buffer) throws SerialPortException {
				System.out.println("SENT: " + new String(buffer));
				return true;
			}
		};

		return s;
	}

}
