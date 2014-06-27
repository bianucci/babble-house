#include <actuating.h>

void handleServiceRequests(UARTMessage* request){
	
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
							changeStateActuatorTwo((uint8_t)request->service.value);
						break;

						case 3:
							changeStateActuatorThree((uint8_t)request->service.value);
						break;
					}
				break;
				
				case Service_ServiceType_SENSOR:
					if(log_enabled){sendUart((uint8_t*)"ZEDRRS\n\r", sizeof("ZEDRRS\n\r"));}
					
				break;
			}
			
		break;
	}
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
	if(newState==1){
		if(log_enabled){sendUart((uint8_t*)"A1A\r", sizeof("A1A\n\r"));}
		PORTE |= (1<<PE0);
	}else{
		if(log_enabled){sendUart((uint8_t*)"A1D\n\r", sizeof("A1D\n\r"));}
		PORTE &= ~(1<<PE0);
	}
}

void changeStateActuatorTwo(uint8_t newState){
	if(newState==1){
		if(log_enabled){sendUart((uint8_t*)"A2A\r", sizeof("A2A\r"));}
		PORTE |= (1<<PE1);
	}else{
		if(log_enabled){sendUart((uint8_t*)"A2D\r", sizeof("A2D\r"));}
		PORTE &= ~(1<<PE1);
	}
}

void changeStateActuatorThree(uint8_t newState){
	if(newState==1){
		if(log_enabled){sendUart((uint8_t*)"A3A\r", sizeof("A3A\r"));}
		PORTE |= (1<<PE2);
	}else{
		if(log_enabled){sendUart((uint8_t*)"A3D\r", sizeof("A3D\r"));}
		PORTE &= ~(1<<PE2);
	}
}
