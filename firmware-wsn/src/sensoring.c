#include <sensoring.h>

#define LIGHT_SENSOR_ID 0

uint8_t sensorRotation=0;

uint8_t lightData;
uint8_t tempData[2];
uint8_t humiData[2];

uint8_t lightSensorValue;
uint16_t tempSensorValue;
uint16_t humiSensorValue;

void readLightSensorDonceCb(void);

void (*refreshDoneCallBack)(uint8_t sensorId, uint32_t value);

uint8_t refreshLightValue(void);
uint8_t refreshTempValue(void);
uint8_t refreshHumiValue(void);

HAL_AdcDescriptor_t adcdescriptor = {
	.resolution=RESOLUTION_8_BIT,
	.sampleRate=ADC_4800SPS,
	.voltageReference=AVCC,
	.bufferPointer=&lightData,
	.selectionsAmount=1,
	.callback = readLightSensorDonceCb
};

HAL_I2cDescriptor_t i2cDescriptor;

void initSensors(){
	HAL_OpenAdc(&adcdescriptor);
}

void readLightSensorDonceCb(){
	lightSensorValue=lightData;
	refreshDoneCallBack(LIGHT_SENSOR_ID, lightSensorValue);
}

void refreshSensorValues(void (*callBack)(uint8_t sensorId, uint32_t value)){
	refreshDoneCallBack=callBack;

	switch(sensorRotation){
		case 0:			
			refreshLightValue();
			break;
		
		case 1:
			refreshTempValue();
			break;

		case 2:
			refreshHumiValue();
			break;

		case 3:
			sensorRotation=0;
			break;
	}
	//sensorRotation++;
}

uint8_t refreshLightValue(){
	return HAL_ReadAdc(&adcdescriptor, HAL_ADC_CHANNEL_1);
}

uint8_t refreshTempValue(){
	return 0;
}

uint8_t refreshHumiValue(){
	return 0;
}

int getLightADC(){return lightSensorValue;}
int getHumiValue(){return humiSensorValue;}
int getTempValue(){return tempSensorValue;}