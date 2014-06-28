#include <sensoring.h>

#define LIGHT_SENSOR_ID 0
#define TEMP_SENSOR_ID	1
#define BATTERY_ID 0
#define PRESSURE_SENSOR_ID	1

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

uint8_t adcData;
uint8_t i2cData[2];

void readADCSensorDonceCb(void);
void readI2CSensorDone(bool result);

void (*refreshDoneCallBack)(uint8_t sensorId, uint32_t value);

uint8_t refreshLightValue(void);
uint8_t refreshTempValue(void);
uint8_t refreshPressureValue(void);
uint8_t refreshBatteryValue(void);

HAL_AdcDescriptor_t adcdescriptor = {
	.resolution=RESOLUTION_8_BIT,
	.sampleRate=ADC_4800SPS,
	.voltageReference=AVCC,
	.bufferPointer=&adcData,
	.selectionsAmount=1,
	.callback = readADCSensorDonceCb
};

static uint8_t lm73Command = 243;
static uint8_t bmp180Command = 0x2e;

static HAL_I2cDescriptor_t i2cDescriptor = {
	.tty = TWI_CHANNEL_0,
	.clockRate = I2C_CLOCK_RATE_62,
	.lengthAddr = HAL_NO_INTERNAL_ADDRESS,
	.f = 0,
	.id = 0x40,
	.data = &lm73Command,
	.length = 1
};


void initSensors(){
	HAL_OpenAdc(&adcdescriptor);
}

void readADCSensorDonceCb(){
	refreshDoneCallBack(LIGHT_SENSOR_ID, adcData);
	sensorRotation=TEMP_SENSOR_ID;
}

void refreshSensorValues(void (*callBack)(uint8_t sensorId, uint32_t value)){
	
	refreshDoneCallBack=callBack;

	#ifdef SERVICE_GROUP_ONE
		
		switch(sensorRotation){
			case LIGHT_SENSOR_ID:			
				refreshLightValue();
				break;
		
			case TEMP_SENSOR_ID:
				refreshTempValue();
				break;
		}
		
	#endif
	#ifdef SERVICE_GROUP_TWO
		
		switch(sensorRotation){
			case BATTERY_ID:			
				refreshLightValue();
				break;
		
			case PRESSURE_SENSOR_ID:
				refreshPressureValue();
				break;
		}	
	#endif
}

uint8_t refreshBatteryValue(){
	return 0;
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
			}else{
				return -1;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_OPENED:
			if(HAL_WriteI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvw\n\r", sizeof("rtvw\n\r"));
				i2cState=I2C_WRITTEN;
			}else{
				return -1;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_WRITTEN:
			i2cDescriptor.f = readI2CSensorDone;
			i2cDescriptor.id = 0x4D;
			i2cDescriptor.data = i2cData;
			i2cDescriptor.length = 2;
			if(HAL_ReadI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvs\n\r", sizeof("rtvs\n\r"));
				i2cState=I2C_READING;
			}else{
				return -1;
			}
		break;
	}	
	return 1;
}

uint8_t refreshPressureValue(){
	switch(i2cState){
		case I2C_CLEAR:
			i2cDescriptor.id = 0x77;
			if(HAL_OpenI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvo\n\r", sizeof("rtvo\n\r"));
				i2cState=I2C_OPENED;
			}else{
				return -1;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_OPENED:
			i2cDescriptor.id = 0xf4;
			i2cDescriptor.data = &bmp180Command;
			if(HAL_WriteI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvw\n\r", sizeof("rtvw\n\r"));
				i2cState=I2C_WRITTEN;
			}else{
				return -1;
			}
			HAL_StartAppTimer(&repeatTimer);
			break;
		
		case I2C_WRITTEN:
			i2cDescriptor.f = readI2CSensorDone;
			i2cDescriptor.id = 0xf7;
			i2cDescriptor.data = i2cData;
			i2cDescriptor.length = 2;
			if(HAL_ReadI2cPacket(&i2cDescriptor) != -1){
				sendUart((uint8_t*)"rtvs\n\r", sizeof("rtvs\n\r"));
				i2cState=I2C_READING;
			}else{
				return -1;
			}
		break;
	}	
	return 1;
}

uint8_t refreshHumiValue(){
	return 0;
}

void readI2CSensorDone(bool result){
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
		
		#ifdef SERVICE_GROUP_ONE
			sensorRotation=LIGHT_SENSOR_ID;
			refreshDoneCallBack(TEMP_SENSOR_ID, meassured);
		#endif
		#ifdef SERVICE_GROUP_TWO
			sensorRotation=BATTERY_ID;
			refreshDoneCallBack(PRESSURE_SENSOR_ID, meassured);
		#endif
		
	}else{
		sendUart((uint8_t*)"rtve\n\r", sizeof("rtve\n\r"));
	}
	i2cDescriptor.f = 0;
	i2cDescriptor.id = 0x40;
	HAL_CloseI2cPacket(&i2cDescriptor);
	i2cState=I2C_CLEAR;
}