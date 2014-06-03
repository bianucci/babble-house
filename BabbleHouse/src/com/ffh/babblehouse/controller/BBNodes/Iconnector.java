package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;

public interface Iconnector {
	// method that returns the serial port  connection
	SerialPort PortConnection();

}
