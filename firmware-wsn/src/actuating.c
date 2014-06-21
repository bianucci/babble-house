#include <actuating.h>

void handleServiceRequests(UARTMessage* request){
	
	changeStateActuatorOne(0);
	
	/*
	
	switch(request->type){
		case UARTMessage_Type_BEACON:
			if(log_enabled){sendUart((uint8_t*)"E_ZEDRB\n\r", sizeof("E_ZEDRB\n\r"));}
			return; // accidentally received beacon message on a end device
		break;
		
		case UARTMessage_Type_SERVICE:
			if(log_enabled){sendUart((uint8_t*)"ZEDRR\n\r", sizeof("ZEDRR\n\r"));}
			
			switch(request->service.serviceType){
			
				case Service_ServiceType_ACTUATOR:
					if(log_enabled){sendUart((uint8_t*)"ZEDRRA\n\r", sizeof("ZEDRRA\n\r"));}
					
					switch(request->service.serviceId){
					
						case 1:
							changeStateActuatorOne((uint8_t)request->service.value);
						break;
						
						case 2:
						break;

						case 3:
						break;
					
					}
					
				break;
				
				case Service_ServiceType_SENSOR:
					if(log_enabled){sendUart((uint8_t*)"ZEDRRS\n\r", sizeof("ZEDRRS\n\r"));}
					
				break;
			}
			
		break;
	}
	
	*/
	
	appState=APP_IDLE;
	SYS_PostTask(APL_TASK_ID);
}

void initActuators(){
	if(log_enabled){sendUart((uint8_t*)"iAct\n\r", sizeof("iAct\n\r"));}
	DDRE |= (1<<PE0);
	DDRE |= (1<<PE1);
	DDRE |= (1<<PE2);
}

void changeStateActuatorOne(uint8_t newState){
	if(newState==0){
		if(log_enabled){sendUart((uint8_t*)"ACT1OFF\n\r", sizeof("ACT1OFF\n\r"));}
		PORTE ^= (1<<PE0);
		PORTE ^= (1<<PE1);
		PORTE ^= (1<<PE2);
	}else{
		if(log_enabled){sendUart((uint8_t*)"ACT1ON\n\r", sizeof("ACT1ON\n\r"));}
		PORTE ^= (1<<PE0);
		PORTE ^= (1<<PE1);
		PORTE ^= (1<<PE2);
	}
}

