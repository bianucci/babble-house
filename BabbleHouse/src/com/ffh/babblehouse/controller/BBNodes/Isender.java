package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;


public interface Isender {
	boolean SenderMessage(UARTMessage uartMessage );
}
