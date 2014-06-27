#ifndef SENSORTING_H
#define SENSORING_H

#include <i2cPacket.h>
#include <halAdc.h>
#include <adc.h>
#include <messaging.h>
#include <app.h>

/* Channels */
#define HAL_ADC_CHANNEL_0          0x00
#define HAL_ADC_CHANNEL_1          0x01
#define HAL_ADC_CHANNEL_2          0x02
#define HAL_ADC_CHANNEL_3          0x03
#define HAL_ADC_CHANNEL_4          0x04
#define HAL_ADC_CHANNEL_5          0x05
#define HAL_ADC_CHANNEL_6          0x06
#define HAL_ADC_CHANNEL_7          0x07

void refreshSensorValues(void (*callBack)(uint8_t sensorId, uint32_t value));

void initSensors();
 
int getLightADC();
int getHumiValue();
int getTempValue();

#endif