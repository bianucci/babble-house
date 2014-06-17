package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;

public abstract class IConnector {

	static IConnector c = null;

	public static IConnector getInstance() {
		if (c == null) {
			try {
				c = new UARTConnector();
			} catch (SerialPortException e) {
				c = new DummyConnector();
			}
		}
		return c;
	}

	public abstract SerialPort getserialPort();
}
