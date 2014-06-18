#ifndef SENSORTING_H
#define SENSORING_H

#include <halAdc.h>
#include <adc.h>

/* Channels */
#define HAL_ADC_CHANNEL_0          0x00
#define HAL_ADC_CHANNEL_1          0x01
#define HAL_ADC_CHANNEL_2          0x02
#define HAL_ADC_CHANNEL_3          0x03
#define HAL_ADC_CHANNEL_4          0x04
#define HAL_ADC_CHANNEL_5          0x05
#define HAL_ADC_CHANNEL_6          0x06
#define HAL_ADC_CHANNEL_7          0x07

extern uint8_t adcData;

void readSensorDonceCb(void);
void refreshSensorValues();
void initSensors();

#endif