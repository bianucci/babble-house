package com.ffh.babblehouse.controller.BBNodes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Beacon;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;

public class DummyConnector extends IConnector {

	@Override
	public SerialPort getserialPort() {
		final Builder serviceBuilder = Service.newBuilder();
		List<Service> sampleService = new ArrayList<UARTMessageProtos.Service>();
		for (int i = 0; i < 6; i++) {
			serviceBuilder
					.setInfo(
							"RandomInfo:"
									+ new BigInteger(32, new Random()).toString(32))
					.setServiceGroupId(1).setServiceId(i).setValue(0);
			if (i < 3) {
				serviceBuilder.setServiceType(ServiceType.SENSOR);
			} else {
				serviceBuilder.setServiceType(ServiceType.ACTUATOR);
			}
			sampleService.add(serviceBuilder.build());
			serviceBuilder.clear();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		final Beacon sampleBeacon = Beacon.newBuilder().setName("Living Room")
				.setServiceGroupId(1).addAllService(sampleService).build();

		serviceBuilder.setInfo("Degree Celcius");
		serviceBuilder.setServiceId(1);
		serviceBuilder.setServiceType(ServiceType.SENSOR);
		serviceBuilder.setServiceGroupId(2);

		SerialPort s = new SerialPort("") {

			boolean sentBeacon = false;

			private int TEMPMAX = 100;

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

				Random r = new Random();
				int randVal = r.nextInt(TEMPMAX);
				serviceBuilder.setValue(randVal);
				Service service = serviceBuilder.build();

				UARTMessage uartMessage = null;
				if (sentBeacon) {
					uartMessage = UARTMessage.newBuilder()
							.setType(Type.SERVICE).setService(service).build();
				} else {
					uartMessage = UARTMessage.newBuilder().setType(Type.BEACON)
							.setBeacon(sampleBeacon).build();
					sentBeacon = true;
				}

				if (byteCount == 1) {
					if (uartMessage.getType().equals(Type.BEACON)) {
						sentBeacon = false;
					}
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
