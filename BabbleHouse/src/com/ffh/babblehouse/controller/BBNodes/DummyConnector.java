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
import com.ffh.babblehouse.controller.BusinessObjects.ExampleStateChangedHandler;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoMeasuringUnit;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.ffh.babblehouse.model.DtoValue;
import com.google.protobuf.InvalidProtocolBufferException;

public class DummyConnector extends IConnector {

	private static DummySerialPort s = null;

	private static class DummySerialPort extends SerialPort {

		public DummySerialPort() {
			super("");

			serviceBuilder = Service.newBuilder();
			List<Service> sampleService = new ArrayList<UARTMessageProtos.Service>();
			for (int i = 0; i < 6; i++) {
				serviceBuilder
						.setInfo(
								"RandomInfo:"
										+ new BigInteger(32, new Random())
												.toString(32))
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

			sampleBeacon = Beacon.newBuilder().setName("Living Room")
					.setServiceGroupId(1).addAllService(sampleService).build();

			serviceBuilder.setInfo("Degree Celcius");
			serviceBuilder.setServiceId(1);
			serviceBuilder.setServiceType(ServiceType.SENSOR);
			serviceBuilder.setServiceGroupId(2);

		}

		boolean sentBeacon = false;

		private int TEMPMAX = 100;

		private Builder serviceBuilder;

		private Beacon sampleBeacon;

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
				uartMessage = UARTMessage.newBuilder().setType(Type.SERVICE)
						.setService(service).build();
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return uartMessage.toByteArray();
			}
		}

		@Override
		public boolean writeBytes(byte[] buffer) throws SerialPortException {
			try {
				System.out.println("SENT: "
						+ dtoToString(UARTMessageProtos.UARTMessage
								.parseFrom(buffer)));
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			return true;
		}

		public String dtoToString(final UARTMessage u) {
			if (u.getService().getServiceType().equals(ServiceType.ACTUATOR)) {
				DtoDevice d = new DtoDevice();
				d.setDeviceName(u.getService().getInfo());
				d.setId(u.getService().getServiceId());
				d.setDtoServiceGroup(new DtoServiceGroup() {
					public int getId() {
						return u.getService().getServiceGroupId();
					};
				});
				d.setValues(new ArrayList<DtoValue>());
				DtoValue value = new DtoValue();
				value.setValue(u.getService().getValue());
				d.getValues().add(value);
				d.setUserDefineRules(new ArrayList<DtoUDR>());
				return d.toString();
			} else {
				DtoSensor s = new DtoSensor();
				s.setId(u.getService().getServiceId());
				s.setValues(new ArrayList<DtoValue>());
				DtoValue value = new DtoValue();
				value.setValue(u.getService().getValue());
				s.getValues().add(value);
				s.setMeasuringUnit(new DtoMeasuringUnit() {
					public String getUnit_name() {
						return u.getService().getInfo();
					};
				});
				s.setDtoServiceGroup(new DtoServiceGroup() {
					public int getId() {
						return u.getService().getServiceGroupId();
					};
				});
				s.setUserDefineRules(new ArrayList<DtoUDR>());
				return s.toString();
			}
		}

		@Override
		public boolean writeByte(byte singleByte) throws SerialPortException {
			return true;
		}
	}

	@Override
	public SerialPort getserialPort() {
		if (s == null) {
			s = new DummySerialPort();
			Receiver r = new Receiver(s);
			new ExampleStateChangedHandler(r);
			r.start();
		}
		return s;
	}
}
