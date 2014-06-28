package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;


public interface ISender {
	boolean sendMessage(UARTMessage uartMessage ) throws SerialPortException;
}
