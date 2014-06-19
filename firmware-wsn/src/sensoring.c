#include <sensoring.h>

uint8_t adcData;

uint8_t lightSensorValue;

HAL_AdcDescriptor_t adcdescriptor = {
	.resolution=RESOLUTION_8_BIT,
	.sampleRate=ADC_4800SPS,
	.voltageReference=AVCC,
	.bufferPointer=&adcData,
	.selectionsAmount=1,
	.callback = readSensorDonceCb
};

void readSensorDonceCb(void){
	lightSensorValue=adcData;
}

void initSensors(){
	HAL_OpenAdc(&adcdescriptor);
}

void refreshSensorValues(){
	HAL_ReadAdc(&adcdescriptor, HAL_ADC_CHANNEL_1);
}

int getLightADC(){
	return lightSensorValue;
}