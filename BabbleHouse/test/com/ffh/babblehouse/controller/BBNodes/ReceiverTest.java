package com.ffh.babblehouse.controller.BBNodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import jssc.SerialPort;
import jssc.SerialPortException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ffh.babblehouse.controller.BusinessObjects.IBoStateChangedHandler;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;

public class ReceiverTest {

	private UARTConnector connector;
	private SerialPort serialPort;

	private class MockStateChangeHandler implements IBoStateChangedHandler {
		public boolean sensorDataChangedCalled = false;
		public boolean deviceDataChangedCalled = false;

		@Override
		public void sensorDataChanged(DtoSensor updatedDtoSensor) {
			sensorDataChangedCalled = true;
			if (updatedDtoSensor != null)
				System.out.println(updatedDtoSensor);
		}

		@Override
		public void deviceDataChanged(DtoDevice updatedDtoDevice) {
			deviceDataChangedCalled = true;
			if (updatedDtoDevice != null)
				System.out.println(updatedDtoDevice);
		}

		@Override
		public void newServiceGroupArrived(DtoServiceGroup newServiceGroup) {
			System.out.println(newServiceGroup);
		}
	};

	@Before
	public void openSerialConnection() throws SerialPortException {
		connector = new UARTConnector();
		serialPort = connector.getserialPort();
	}

	@After
	public void closeSerialConnection() throws SerialPortException {
		boolean closePort = serialPort.closePort();
		connector = null;
		serialPort = null;
		assertTrue(closePort);
	}

	@Test
	public void testReceiver() throws SerialPortException {
		Receiver receiver = new Receiver(serialPort);
		assertNotNull(receiver);
	}

	@Test
	public void testRegisterChangeHandler() {
		Receiver receiver = new Receiver(serialPort);
		IBoStateChangedHandler ibbDataBridge = new MockStateChangeHandler();
		receiver.registerChangeHandler(ibbDataBridge);
		assertEquals(ibbDataBridge, receiver.getChangeHandler());
	}

	@Test
	public void testSensorValueChanged() {
		Receiver receiver = new Receiver(serialPort);
		MockStateChangeHandler ibbDataBridge = new MockStateChangeHandler();
		receiver.registerChangeHandler(ibbDataBridge);

		assertFalse(ibbDataBridge.deviceDataChangedCalled);
		assertFalse(ibbDataBridge.sensorDataChangedCalled);
		receiver.valueChanged(new DtoSensor());
		assertFalse(ibbDataBridge.deviceDataChangedCalled);
		assertTrue(ibbDataBridge.sensorDataChangedCalled);
	}

	@Test
	public void testDeviceValueChanged() {
		Receiver receiver = new Receiver(serialPort);
		MockStateChangeHandler ibbDataBridge = new MockStateChangeHandler();
		receiver.registerChangeHandler(ibbDataBridge);

		assertFalse(ibbDataBridge.deviceDataChangedCalled);
		assertFalse(ibbDataBridge.sensorDataChangedCalled);
		receiver.valueChanged(new DtoDevice());
		assertTrue(ibbDataBridge.deviceDataChangedCalled);
		assertFalse(ibbDataBridge.sensorDataChangedCalled);
	}

	@Test
	public void testRun() {
		Receiver receiver = new Receiver(serialPort);
		receiver.registerChangeHandler(new MockStateChangeHandler());
		for (int i = 0; i < 5; i++) {
			receiver.receive();
		}
	}

}
