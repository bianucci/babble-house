/**************************************************************************//**
  \file blink.h

  \brief Blink application header file.

  \author
    Atmel Corporation: http://www.atmel.com \n
    Support email: avr@atmel.com

  Copyright (c) 2008-2012, Atmel Corporation. All rights reserved.
  Licensed under Atmel's Limited License Agreement (BitCloudTM).

  \internal
    History:
******************************************************************************/

#ifndef _BLINK_H
#define _BLINK_H

/******************************************************************************
                    Includes section
******************************************************************************/
#include "sliders.h"
#include "buttons.h"
#include "leds.h"
#include "uartmessage.pb.h"
#include "pb_encode.h"
#include "pb_decode.h"
#include "pb.h"
#include "configuration.h"
#include <halAdc.h>
#include <adc.h>

/******************************************************************************
                    Defines section
******************************************************************************/
#define APP_BLINK_INTERVAL             (APP_BLINK_PERIOD / 2)       // Blink interval.
#define APP_MIN_BLINK_INTERVAL         (APP_MIN_BLINK_PERIOD / 2)   // Minimum blink interval.
#define APP_MAX_BLINK_INTERVAL         (APP_MAX_BLINK_PERIOD / 2)   // Maximum blink interval.

#define APP_HALF_PERIOD_BUTTON          BSP_KEY0                // Button that reduces blink interval to a half.
#define APP_DOUBLE_PERIOD_BUTTON        BSP_KEY1                // Button that doubles blink interval.

#define APP_UART_SEND_INTERVAL			1000

#define OPEN_USART						HAL_OpenUsart
#define CLOSE_USART						HAL_CloseUsart
#define WRITE_USART						HAL_WriteUsart
#define READ_USART						HAL_ReadUsart
#define USART_CHANNEL					USART_CHANNEL_1
#define USART_RX_BUFFER_LENGTH			0

#define TEXT_ONE "BRUNOOO\r"
#define TEXT_TWO "MARTINN\r"
#define TEXT_THR "CLAUDIO\r"

/* Channels */
#define HAL_ADC_CHANNEL_0          0x00
#define HAL_ADC_CHANNEL_1          0x01
#define HAL_ADC_CHANNEL_2          0x02
#define HAL_ADC_CHANNEL_3          0x03
#define HAL_ADC_CHANNEL_4          0x04
#define HAL_ADC_CHANNEL_5          0x05
#define HAL_ADC_CHANNEL_6          0x06
#define HAL_ADC_CHANNEL_7          0x07

void appInitUARTManager(void);
void appWriteDataToUart(uint8_t* aData, uint8_t aLength);

typedef enum{
	APP_INIT_SYSTEM,
	APP_INIT_UART,
	APP_INIT_SENSORS,
	APP_INIT_ENDPOINT,
	APP_START_NETWORK,
	APP_ZGBE_RECVD,
	APP_UART_RECVD,
	APP_SEND_BEACON,
	APP_SEND_SERVICE_REQST,
	APP_SEND_SERVICE_RSPNS,
	APP_READ_ADC,
	APP_IDLE
} AppState_t;

#endif