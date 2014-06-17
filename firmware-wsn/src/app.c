#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>

static AppState_t appState = APP_INIT_SYSTEM;

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
			appState=APP_START_NETWORK;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_START_NETWORK:
		if(log_enabled){sendUart((uint8_t*)"APP_START_NETWORK\n\r", sizeof("APP_START_NETWORK\n\r"));}
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_IDLE;
			#else
				appState=APP_INIT_ENDPOINT;
			#endif
		break;
		
		case APP_INIT_ENDPOINT:
		if(log_enabled){sendUart((uint8_t*)"APP_INIT_ENDPOINT\n\r", sizeof("APP_INIT_ENDPOINT\n\r"));}
			appState=APP_SEND_BEACON;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_SEND_BEACON:
		if(log_enabled){sendUart((uint8_t*)"APP_SEND_BEACON\n\r", sizeof("APP_SEND_BEACON\n\r"));}
			appState=APP_IDLE;
		break;
		
		case APP_UART_RCVD:
		if(log_enabled){sendUart((uint8_t*)"APP_UART_RCVD\n\r", sizeof("APP_UART_RCVD\n\r"));}
			appState=APP_SEND_SERVICE_REQST;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_SEND_SERVICE_REQST:
		if(log_enabled){sendUart((uint8_t*)"APP_SEND_SERVICE_REQST\n\r", sizeof("APP_SEND_SERVICE_REQST\n\r"));}
			appState=APP_IDLE;
		break;
		
		case APP_ZGBE_RCVD:
		if(log_enabled){sendUart((uint8_t*)"APP_ZGBE_RCVD\n\r", sizeof("APP_ZGBE_RCVD\n\r"));}
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_UART_SEND;
			#else
				appState=APP_READ_ADC;
			#endif
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_UART_SEND:
		if(log_enabled){sendUart((uint8_t*)"APP_UART_SEND\n\r", sizeof("APP_UART_SEND\n\r"));}
			appState=APP_IDLE;
		break;
		
		case APP_READ_ADC:
		if(log_enabled){sendUart((uint8_t*)"APP_READ_ADC\n\r", sizeof("APP_READ_ADC\n\r"));}
			appState=APP_SEND_SERVICE_RSPNS;
			SYS_PostTask(APL_TASK_ID);
		break;
		
		case APP_SEND_SERVICE_RSPNS:
		if(log_enabled){sendUart((uint8_t*)"APP_SEND_SERVICE_RSPNS\n\r", sizeof("APP_SEND_SERVICE_RSPNS\n\r"));}
			appState=APP_IDLE;
		break;
		
		case APP_IDLE:
		if(log_enabled){sendUart((uint8_t*)"APP_IDLE\n\r", sizeof("APP_IDLE\n\r"));}
		break;
	}
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