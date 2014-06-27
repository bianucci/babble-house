#include <messaging.h>

static uint8_t Rx_Buffer[RX_BUFFER_SIZE];
static uint8_t Tx_Buffer[TX_BUFFER_SIZE];
static uint8_t buffer[RX_BUFFER_SIZE];

Beacon my_beacon;

UARTMessage globalMessage;
uint8_t global_dst;

uint8_t messageRerceived_length;

HAL_UsartDescriptor_t usart;

uint8_t buffer_counter = 0;
uint8_t message_length = 0;
uint8_t last_msg[RX_BUFFER_SIZE]; 
uint8_t last_msg_length=0;

HAL_UsartDescriptor_t* getStandardDescriptor(){
	return &usart;
}

void usartRcvd(uint8_t size)
{
	if(message_length==0){
		// new message is going to be received. read its length and store it.
		HAL_ReadUsart(&usart, &message_length, sizeof(message_length));
		size--; 
	}
	
	uint8_t data; 

	for(int i=0; i<size; i++) 
	{ 
		HAL_ReadUsart(&usart, &data, sizeof(data)); 
		buffer[buffer_counter]=data;
		buffer_counter++;
	
		if(buffer_counter==message_length){
			memcpy(last_msg, buffer, message_length);
			last_msg_length=message_length;
			buffer_counter=0;
			message_length=0;
			
			if(log_enabled){sendUart((uint8_t*)"UTTP\n\r", sizeof("UTTP\n\r"));}
			pb_istream_t istream = pb_istream_from_buffer(last_msg, last_msg_length);
			bool status = pb_decode(&istream, UARTMessage_fields, &globalMessage);
			if(status){
				appState=APP_ZGBE_SEND;
				SYS_PostTask(APL_TASK_ID);
			}
		}
	}
}

void createBeaconList(){
	if(log_enabled){sendUart((uint8_t*)"cBL\n\r", sizeof("cBL\n\r"));}
	
	my_beacon.serviceGroupId=1;
	my_beacon.has_name=true;
	strcpy(my_beacon.name,"living room");
	my_beacon.service_count=2;
	
	for(int i=0; i<2; i++){
		my_beacon.service[i].serviceGroupId=1;
		my_beacon.service[i].serviceId=i;
		my_beacon.service[i].has_value=true;
		my_beacon.service[i].has_info=true;
	
		// assign random values
		if(i<1){	
			my_beacon.service[i].serviceType=Service_ServiceType_SENSOR;
			my_beacon.service[i].value=rand() % 100;
			switch(i){
				case 0: strcpy(my_beacon.service[i].info, "temperature"); break; 
				//case 1: strcpy(my_beacon.service[i].info, "light"); break;
				//case 2: strcpy(my_beacon.service[i].info, "humidity"); break;
				//case 3: strcpy(my_beacon.service[i].info, "pressure"); break;	
			}
		} else {
			my_beacon.service[i].serviceType=Service_ServiceType_ACTUATOR;
			my_beacon.service[i].value=i%2;
			switch(i){
				case 1: strcpy(my_beacon.service[i].info, "heater"); break; 
				//case 3: strcpy(my_beacon.service[i].info, "light bulb"); break;
				//case 6: strcpy(my_beacon.service[i].info, "radio"); break;
				//case 7: strcpy(my_beacon.service[i].info, "television"); break;	
			}
		}
		
	}
}

void usart_Init()
{	
  usart.tty             = USART_CHANNEL_1;
  usart.mode            = USART_MODE_ASYNC;
  usart.baudrate        = USART_BAUDRATE_38400;
  usart.dataLength      = USART_DATA8;
  usart.parity          = USART_PARITY_NONE;
  usart.stopbits        = USART_STOPBIT_1;
  usart.rxBuffer        = Rx_Buffer;
  usart.rxBufferLength  = RX_BUFFER_SIZE;
  usart.txBuffer        = Tx_Buffer;
  usart.txBufferLength  = TX_BUFFER_SIZE;
  usart.rxCallback      = usartRcvd;
  usart.txCallback      = NULL;
  usart.flowControl     = USART_FLOW_CONTROL_NONE;
  HAL_OpenUsart(&usart);
}

void sendUart(uint8_t* data, uint8_t size){
	HAL_WriteUsart(&usart, data, size);
}

void assembleBeaconMessage(){
	if(log_enabled){sendUart((uint8_t*)"asmblBcnMsg\r", sizeof("asmblBcnMsg\r"));}
	globalMessage.has_service=false;
	globalMessage.beacon=my_beacon;
	globalMessage.type=UARTMessage_Type_BEACON;
	globalMessage.has_beacon=true;
}

void assembleSensorServiceMessage(uint8_t serviceId, uint32_t value){
	if(log_enabled){sendUart((uint8_t*)"asmblSrvMsg\r", sizeof("asmblBcnMsg\r"));}
	Service* my_service = &my_beacon.service[serviceId];
	my_service->has_value=true;
	my_service->value=value;

	globalMessage.has_service=true;
	globalMessage.service=*my_service;
	globalMessage.type=UARTMessage_Type_SERVICE;
	globalMessage.has_beacon=false;
}

static uint8_t encBuffer[100];
void forwardMessageToPC(){
	pb_ostream_t ostream = pb_ostream_from_buffer(encBuffer, sizeof(encBuffer));
	pb_encode(&ostream, UARTMessage_fields, &globalMessage);
	uint8_t size = ostream.bytes_written;
	HAL_WriteUsart(&usart,&size,1);
	HAL_WriteUsart(&usart,encBuffer,size);
	SYS_PostTask(APL_TASK_ID);
}