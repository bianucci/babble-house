package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;


public interface Isender {
	boolean SenderMessage(UARTMessage uartMessage );
}
