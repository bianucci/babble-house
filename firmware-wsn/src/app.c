#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>


static AppState_t appState = APP_INIT_STATE;

HAL_AppTimer_t sendTimer;    
HAL_AppTimer_t receiveTimerLed;
HAL_AppTimer_t transmitTimerLed;
HAL_AppTimer_t transmitTimer;
HAL_AppTimer_t readADCTimer;

static void receiveTimerLedFired(void);
static void transmitTimerLedFired(void);
static void transmitTimerFired(void);
static void printMessageFired(void);

static void initTimer(void);

void APL_TaskHandler(void)
{
	switch(appState){
		case APP_INIT_STATE:
			//usart_Init();
			//HAL_OpenAdc(&adcdescriptor);	
			//HAL_OpenUsart(&usart);
			//HAL_WriteUsart(&usart, "APP_INIT_STATE\r\n", sizeof("APP_INIT_STATE\r\n"));
			//initTimer();
			//appState=APP_START_NETWORK_STATE;
			///SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_START_NETWORK_STATE:
			//HAL_WriteUsart(&usart, "APP_START_NETWORK_STATE\r\n", sizeof("APP_START_NETWORK_STATE\r\n"));
			//networkParams.ZDO_StartNetworkConf=ZDO_StartNetworkConf;
			//ZDO_StartNetworkReq(&networkParams);
			//appState=APP_INIT_ENDPOINT_STATE;
			break;
			
		case APP_INIT_ENDPOINT_STATE:
			//HAL_WriteUsart(&usart, "APP_INIT_ENDPOINT_STATE\r\n", sizeof("APP_INIT_ENDPOINT_STATE\r\n"));
			//initEndpoint();
			//#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
			//	appState=APP_NOTHING_STATE;
			//#else
			//	appState=APP_INIT_TRANSMITDATA_STATE;
			//#endif;
			//SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_INIT_TRANSMITDATA_STATE:
			//HAL_WriteUsart(&usart, "APP_INIT_TRANSMITDATA_STATE\r\n", sizeof("APP_INIT_TRANSMITDATA_STATE\r\n")); 		
			//initTransmitData();
			//appState=APP_NOTHING_STATE;
			//HAL_StartAppTimer(&transmitTimer);
			//HAL_StartAppTimer(&readADCTimer);
			//SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_TRANSMIT_STATE:
			//HAL_WriteUsart(&usart, "APP_TRANSMIT_STATE\r\n", sizeof("APP_TRANSMIT_STATE\r\n"));
			//#if CS_DEVICE_TYPE==DEV_TYPE_ENDDEVICE
			//	transmitData.data[0]=adcData;
			//#else
			//	transmitData.data[0]=0;
			//#endif;
			//APS_DataReq(&dataReq);
			//break;
			
		case APP_READ_ADC_STATE:
			//HAL_WriteUsart(&usart, "READ BEGN\r", sizeof("read begn\r"));
			//HAL_ReadAdc(&adcdescriptor, HAL_ADC_CHANNEL_1);
			//appState=APP_TRANSMIT_STATE;
			//#if CS_DEVICE_TYPE==DEV_TYPE_ENDDEVICE
			//	HAL_WriteUsart(&usart, &adcData, 1);
			//#endif
			//appWriteDataToUart((uint8_t*)"read done\r", sizeof("read done\r"));
			//SYS_PostTask(APL_TASK_ID);
			break;
		
		case APP_NOTHING_STATE:
			//HAL_WriteUsart(&usart, "APP_NOTHING_STATE\r\n", sizeof("APP_NOTHING_STATE\r\n"));
			break;
	}
}

static void readADCTimerFired(void){
	appState=APP_READ_ADC_STATE;
	SYS_PostTask(APL_TASK_ID);
}

static void initTimer(void){
	transmitTimerLed.interval=500;
	transmitTimerLed.mode=TIMER_ONE_SHOT_MODE;
	transmitTimerLed.callback=transmitTimerLedFired;
	
	receiveTimerLed.interval=500;
	receiveTimerLed.mode=TIMER_ONE_SHOT_MODE;
	receiveTimerLed.callback=receiveTimerLedFired;

	transmitTimer.interval=1000;
	transmitTimer.mode=TIMER_REPEAT_MODE;
	transmitTimer.callback=transmitTimerFired;
	
	readADCTimer.interval = 1000;
	readADCTimer.mode = TIMER_REPEAT_MODE;
	readADCTimer.callback = readADCTimerFired;
}

static void transmitTimerLedFired(void){
}

static void receiveTimerLedFired(void){
}

static void transmitTimerFired(void){
	appState=APP_TRANSMIT_STATE;
	SYS_PostTask(APL_TASK_ID);
}

void ZDO_MgmtNwkUpdateNotf(ZDO_MgmtNwkUpdateNotf_t *nwkParams) { nwkParams = nwkParams; }
void ZDO_WakeUpInd(void) {}

#ifdef _BINDING_
void ZDO_BindIndication(ZDO_BindInd_t *bindInd){(void)bindInd;}
void ZDO_UnbindIndication(ZDO_UnbindInd_t *unbindInd){(void)unbindInd;}
#endif

int main(void)
{
  SYS_SysInit();
 
  for(;;)
  {
    SYS_RunTask();
  }
}