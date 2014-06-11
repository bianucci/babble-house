

static uint8_t adcData;
static HAL_AppTimer_t readADCTimer;
char str[5];
static void readSensorDonceCb(void);
static void readADCTimerFired(void);

static HAL_AdcDescriptor_t adcdescriptor = {
	.resolution=RESOLUTION_8_BIT,
	.sampleRate=ADC_4800SPS,
	.voltageReference=AVCC,
	.bufferPointer=&adcData,
	.selectionsAmount=1,
	.callback = readSensorDonceCb
};

static void readSensorDonceCb(void){
	sprintf(str, "%d", adcData);
}
