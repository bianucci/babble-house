package com.ffh.babblehouse.controller.BBNodes;

import static org.junit.Assert.*;
import jssc.SerialPortException;

import org.junit.Test;

public class UARTConnectorTest {

	@Test
	public void test() throws SerialPortException {
		UARTConnector uartConnector = new UARTConnector();
		assertNotNull(uartConnector);
		assertTrue(uartConnector.getserialPort().isOpened());
	}

	
}
