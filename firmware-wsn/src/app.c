#include <appTimer.h>
#include <zdo.h>
#include <app.h>

AppState_t appState = APP_INIT_SYSTEM;

void startSensorTimers();
void readADCTimerFired();

bool log_enabled = true;

void APL_TaskHandler(void)
{	
	switch(appState){
		case APP_INIT_SYSTEM:
			if(log_enabled){sendUart((uint8_t*)"APP_INIT_SYSTEM\n\r", sizeof("APP_INIT_SYSTEM\n\r"));}
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				if(log_enabled){
					appState=APP_START_NETWORK;
				}else{
					appState=APP_INIT_UART;
				}
			#else
				appState=APP_INIT_SENSORS;
			#endif
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_INIT_UART:
			usart_Init();
			if(log_enabled){
				sendUart((uint8_t*)"APP_INIT_UART\n\r", sizeof("APP_INIT_UART\n\r"));
				appState=APP_INIT_SYSTEM;
			}else{
				appState=APP_START_NETWORK;
			}
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_INIT_SENSORS:
			if(log_enabled){sendUart((uint8_t*)"APP_INIT_SENSORS\n\r", sizeof("APP_INIT_SENSORS\n\r"));}
			initSensors();
			appState=APP_START_NETWORK;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_START_NETWORK:
			if(log_enabled){sendUart((uint8_t*)"APP_START_NETWORK\n\r", sizeof("APP_START_NETWORK\n\r"));}
			startNetwork();
			appState=APP_INIT_ENDPOINT;
		break;
		
		case APP_INIT_ENDPOINT:
			if(log_enabled){sendUart((uint8_t*)"APP_INIT_ENDPOINT\n\r", sizeof("APP_INIT_ENDPOINT\n\r"));}
			initEndpoint();
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_IDLE;
			#else
				createBeaconList(); // there should be different implementation for depending on the services this device offers.
				assembleUartMessage(255); // adjust next zigbee message for beacon transfer
				//startSensorTimers();
				appState=APP_ZGBE_SEND;
			#endif
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_ZGBE_SEND:
			if(log_enabled){sendUart((uint8_t*)"APP_ZGBE_SEND\n\r", sizeof("APP_ZGBE_SEND\n\r"));}
			send_uart_as_zigbee(&messageToSend); // global zigbee message from messaging.h is send
			appState=APP_IDLE;
		break;
		
		case APP_UART_RCVD:
			if(log_enabled){sendUart((uint8_t*)"APP_UART_RCVD\n\r", sizeof("APP_UART_RCVD\n\r"));}
			appState=APP_ZGBE_SEND;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_CHNG_ACUTATR:
			if(log_enabled){sendUart((uint8_t*)"APP_SEND_SERVICE_REQST\n\r", sizeof("APP_SEND_SERVICE_REQST\n\r"));}
			appState=APP_ZGBE_SEND;
		break;
		
		case APP_ZGBE_RCVD:
		if(log_enabled){sendUart((uint8_t*)"APP_ZGBE_RCVD\n\r", sizeof("APP_ZGBE_RCVD\n\r"));}
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_UART_SEND;
			#else
				appState=APP_CHNG_ACUTATR;
			#endif
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_UART_SEND:
			if(log_enabled){sendUart((uint8_t*)"APP_UART_SEND\n", sizeof("APP_UART_SEND\n"));}
			forwardMessageToPC();
			appState=APP_IDLE;
		break;
		
		case APP_READ_SENSORS:
			if(log_enabled){sendUart((uint8_t*)"APP_READ_ADC\n\r", sizeof("APP_READ_ADC\n\r"));}
			refreshSensorValues();
			appState=APP_ZGBE_SEND;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_IDLE:
			if(log_enabled){sendUart((uint8_t*)"APP_IDLE\n\r", sizeof("APP_IDLE\n\r"));}
		break;
	}
}

HAL_AppTimer_t readADCTimer;
void startSensorTimers(){
	readADCTimer.interval = 3000;
	readADCTimer.mode = TIMER_REPEAT_MODE;
	readADCTimer.callback = readADCTimerFired;
	HAL_StartAppTimer(&readADCTimer);
}
void readADCTimerFired(){
	appState=APP_READ_SENSORS;
	SYS_PostTask(APL_TASK_ID);
}

void wakeUpZigBeeReceived(){
	appState=APP_ZGBE_RCVD;
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
  if(log_enabled){appState=APP_INIT_UART;}
  SYS_SysInit();
 
  for(;;)
  {
    SYS_RunTask();
  }
}