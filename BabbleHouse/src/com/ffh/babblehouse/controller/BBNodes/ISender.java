package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;

import jssc.SerialPortException;


public interface ISender {
	boolean sendMessage(UARTMessage uartMessage ) throws SerialPortException;
}
