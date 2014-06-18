package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;


public interface Isender {
	void SenderMessage(UARTMessage uartMessage ) throws SerialPortException;
}
