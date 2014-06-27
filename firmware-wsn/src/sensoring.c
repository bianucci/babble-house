#include <sensoring.h>

#define LIGHT_SENSOR_ID 0
#define TEMP_SENSOR_ID	1

#define I2C_OPENED		1
#define I2C_WRITTEN		2
#define I2C_READING		3
#define I2C_CLEAR		4

static uint8_t i2cState = 4;

uint8_t sensorRotation=1;

HAL_AppTimer_t repeatTimer = {
	.interval = 1000,
	.mode = TIMER_ONE_SHOT_MODE,
	.callback = refreshSensorsFired
};

uint8_t lightData;
uint8_t i2cData[2];
uint8_t humiData[2];

uint8_t lightSensorValue;
uint16_t tempSensorValue;
uint16_t humiSensorValue;

void readLightSensorDonceCb(void);
void readTempSensorDone(bool result);

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

static uint8_t i2cCommand = 243;
static HAL_I2cDescriptor_t i2cDescriptor = {
	.tty = TWI_CHANNEL_0,
	.clockRate = I2C_CLOCK_RATE_62,
	.lengthAddr = HAL_NO_INTERNAL_ADDRESS,
	.f = 0,
	.id = 0x40,
	.data = &i2cCommand,
	.length = 1
};


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
	switch(i2cState){
		case I2C_CLEAR:
			if(HAL_OpenI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvo\n\r", sizeof("rtvo\n\r"));
				i2cState=I2C_OPENED;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_OPENED:
			if(HAL_WriteI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvw\n\r", sizeof("rtvw\n\r"));
				i2cState=I2C_WRITTEN;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_WRITTEN:
			i2cDescriptor.f = readTempSensorDone;
			i2cDescriptor.id = 0x4D;
			i2cDescriptor.data = i2cData;
			i2cDescriptor.length = 2;
			if(HAL_ReadI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvs\n\r", sizeof("rtvs\n\r"));
				i2cState=I2C_READING;
			}
		break;
	}	
}

uint8_t refreshHumiValue(){
	return 0;
}

void readTempSensorDone(bool result){
	uint32_t meassured;
		
	if(result){
		sendUart((uint8_t*)"rtvx\n\r", sizeof("rtvx\n\r"));
		uint16_t vk; //vorkommastellen
		vk = i2cData[0];
		vk <<= 8;
		vk |= i2cData[1];
		vk >>= 7;
		meassured=vk*100;
	
		uint16_t nk; //nachkommastellen
		nk = i2cData[1] & (0x7F);
		nk >>= 5;
		nk = nk * 25;
		meassured+=nk;
		
		refreshDoneCallBack(TEMP_SENSOR_ID, meassured);
	}else{
		sendUart((uint8_t*)"rtve\n\r", sizeof("rtve\n\r"));
	}
	i2cDescriptor.f = 0;
	i2cDescriptor.id = 0x40;
	HAL_CloseI2cPacket(&i2cDescriptor);
	i2cState=I2C_CLEAR;
}


int getLightADC(){return lightSensorValue;}
int getHumiValue(){return humiSensorValue;}
int getTempValue(){return tempSensorValue;}