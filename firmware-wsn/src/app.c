#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>

static AppState_t appState = APP_INIT_SYSTEM;

void APL_TaskHandler(void)
{
	switch(appState){
		
		case APP_INIT_SYSTEM:
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_INIT_UART;
			#else
				appState=APP_INIT_SENSORS;
			#endif
		break;
		
		case APP_INIT_UART:
			appState=APP_START_NETWORK;
		break;
		
		case APP_INIT_SENSORS:
			appState=APP_START_NETWORK;
		break;
		
		case APP_START_NETWORK:
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_IDLE;
			#else
				appState=APP_INIT_ENDPOINT;
			#endif
		break;
		
		case APP_INIT_ENDPOINT:
			appState=APP_SEND_BEACON;
		break;
		
		case APP_SEND_BEACON:
			appState=APP_IDLE;
		break;
		
		case APP_UART_RCVD:
			appState=APP_SEND_SERVICE_REQST;
		break;
		
		case APP_SEND_SERVICE_REQST:
			appState=APP_IDLE;
		break;
		
		case APP_ZGBE_RCVD:
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_UART_SEND;
			#else
				appState=APP_READ_ADC;
			#endif
		break;
		
		case APP_UART_SEND:
			appState=APP_IDLE;
		break;
		
		case APP_READ_ADC:
			appState=APP_SEND_SERVICE_RSPNS;
		break;
		
		case APP_SEND_SERVICE_RSPNS:
			appState=APP_IDLE;
		break;
		
		case APP_IDLE:
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
  SYS_SysInit();
 
  for(;;)
  {
    SYS_RunTask();
  }
}