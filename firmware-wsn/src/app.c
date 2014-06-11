#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>

static AppState_t appState = APP_INIT_STATE;

void APL_TaskHandler(void)
{
	switch(appState){
		
		case APP_INIT_STATE:
			break;
			
		case APP_START_NETWORK_STATE:
			break;
			
		case APP_INIT_ENDPOINT_STATE:
			break;
			
		case APP_INIT_TRANSMITDATA_STATE:
			break;
			
		case APP_TRANSMIT_STATE:
			break;
			
		case APP_READ_ADC_STATE:
			break;
		
		case APP_NOTHING_STATE:
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