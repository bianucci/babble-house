/**************************************************************************//**
  \file uartManager.c

  \brief Troughput test application UART manager implementation.

  \author
    Atmel Corporation: http://www.atmel.com \n
    Support email: avr@atmel.com

  Copyright (c) 2008-2012, Atmel Corporation. All rights reserved.
  Licensed under Atmel's Limited License Agreement (BitCloudTM).

  \internal
    History:
******************************************************************************/

#include <types.h>
#include <app.h>
#include <usart.h>

#if BSP_ENABLE_RS232_CONTROL == 1
#include "rs232Controller.h"
#endif // BSP_ENABLE_RS232_CONTROL

/***********************************************************************************
  Local variables
 ***********************************************************************************/
static bool uartBusy = false; // UART busy flag
static uint8_t writePending = 0; // Amount of bytes pending for UART transmission
static uint8_t tmpBuf[100]; // Temporary buffer for transmissions to UART

static HAL_UsartDescriptor_t appUartDescriptor; // UART description

extern void appWriteDataToUart(uint8_t* aData, uint8_t aLength); // Data transmission to UART
static void appUartWriteConfirm(void); // UART writing confirmation
void copyStrToBuffer(uint8_t* aData, uint8_t aLenth);

/********************************************************************
  Description: UART writing confirmation has been received. New message can be sent.

  Parameters: none.

  Returns: nothing.
********************************************************************/
static void appUartWriteConfirm(void)
{
  uartBusy = false; // UART port has been released

  // There is data waiting to be written to UART
  if (writePending)
  {
    appWriteDataToUart(tmpBuf, writePending); // Write temporary stored data to UART
    writePending = 0;
  }
}

/********************************************************************
  Description: Init UART, register UART callbacks.

  Parameters: none.

  Returns: nothing.
********************************************************************/
void appInitUARTManager(void)
{
  appUartDescriptor.tty            = USART_CHANNEL;
  appUartDescriptor.mode           = USART_MODE_ASYNC;        // UART synchronization mode
  appUartDescriptor.baudrate       = USART_BAUDRATE_38400;    // UART baud rate
  appUartDescriptor.dataLength     = USART_DATA8;             // UART data length
  appUartDescriptor.parity         = USART_PARITY_NONE;       // UART parity mode.
  appUartDescriptor.stopbits       = USART_STOPBIT_1;         // UART stop bit
  appUartDescriptor.flowControl    = USART_FLOW_CONTROL_NONE; // Flow control
  appUartDescriptor.rxBuffer       = NULL;
  appUartDescriptor.rxBufferLength = 0;
  appUartDescriptor.txBuffer       = NULL;
  appUartDescriptor.txBufferLength = 0;
  appUartDescriptor.txCallback     = appUartWriteConfirm;  // Callback function, confirming data writing

#if BSP_ENABLE_RS232_CONTROL == 1
  BSP_EnableRs232();
#endif // BSP_ENABLE_RS232_CONTROL

  OPEN_USART(&appUartDescriptor); // Open UART using specified parameters
}

/********************************************************************
  Description: Create and send message to the UART

  Parameters: aData - frame being sent to UART
              aLength - size of the frame in bytes

  Returns: nothing.
********************************************************************/
void appWriteDataToUart(uint8_t* aData, uint8_t aLength)
{
  // UART port is occupied
  if (uartBusy)
  {
    //strncpy((char*) tmpBuf, (char*) aData, aLength); // Temporary store message in special buffer
    //writePending += aLength; // Increase data size stored in the temporary buffer
	copyStrToBuffer(aData, aLength);
  }
  // UART port is free
  else
  {
    uartBusy = true; // Mark UART port as occupied
    WRITE_USART(&appUartDescriptor, (void*) aData, aLength); // Transmit data to UART
  }
}

void copyStrToBuffer(uint8_t* aData, uint8_t aLenth){
	for (int i=0; i<aLenth; i++)
	{
		if (writePending<=sizeof(tmpBuf))
		{
			tmpBuf[writePending]=aData[i];
			writePending++;
		}
		
	}
	
}

//eof uartManager.c
