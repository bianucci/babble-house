#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>

static AppState_t appState = APP_INIT_STATE;

void APL_TaskHandler(void)
{
	switch(appState){
		
		case APP_INIT_SYSTEM:
		break;
		
		case APP_INIT_UART:
		break;
		
		case APP_INIT_SENSORS:
		break;
		
		case APP_INIT_ENDPOINT:
		break;
		
		case APP_START_NETWORK:
		break;
		
		case APP_ZGBE_RECVD:
		break;
		
		case APP_UART_RECVD:
		break;
		
		case APP_SEND_BEACON:
		break;
		
		case APP_SEND_SERVICE_REQST:
		break;
		
		case APP_SEND_SERVICE_RSPNS:
		break;
		
		case APP_READ_ADC:
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