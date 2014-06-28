package com.ffh.babblehouse.controller.BBNodes;

import static org.junit.Assert.*;
import jssc.SerialPort;
import jssc.SerialPortException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;

public class SenderTest {

	private UARTConnector connector;
	private SerialPort serialPort;

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
	public void testSender() {
		Sender sender = new Sender(serialPort);
		assertNotNull(sender);
	}

	@Test
	public void testSenderMessage() throws SerialPortException {
		Sender sender = new Sender(serialPort);
		boolean sent = sender.sendMessage(UARTMessage.getDefaultInstance());
		assertTrue(sent);
	}
}
