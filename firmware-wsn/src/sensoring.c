#include <sensoring.h>

uint8_t adcData;
char str[5];

HAL_AdcDescriptor_t adcdescriptor = {
	.resolution=RESOLUTION_8_BIT,
	.sampleRate=ADC_4800SPS,
	.voltageReference=AVCC,
	.bufferPointer=&adcData,
	.selectionsAmount=1,
	.callback = readSensorDonceCb
};

void readSensorDonceCb(void){
	sprintf(str, "%d", adcData);
}

void initSensors(){
	HAL_OpenAdc(&adcdescriptor);
}

void refreshSensorValues(){
	HAL_ReadAdc(&adcdescriptor, HAL_ADC_CHANNEL_1);
}