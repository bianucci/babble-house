

static uint8_t Rx_Buffer[RX_BUFFER_SIZE];
static uint8_t Tx_Buffer[TX_BUFFER_SIZE];
static uint8_t buffer[RX_BUFFER_SIZE];


static Beacon my_beacon;

static uint8_t beacon_sent=0;


static HAL_UsartDescriptor_t usart;

static int buffer_counter = 0;
static uint8_t message_length = 0;


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
			
			//HAL_WriteUsart(&usart,&last_msg_length, sizeof(last_msg_length));
			//HAL_WriteUsart(&usart,last_msg,last_msg_length);
			
			buffer_counter=0;
			message_length=0;
		}
	}
}

static void printMessageFired(){

	// DECODING LAST MESSAGE RECEIVED
	UARTMessage uart_message;
	/* Create a stream that reads from the buffer. */
    pb_istream_t istream = pb_istream_from_buffer(last_msg, last_msg_length);
	/* Now we are ready to decode the message. */
	bool status = pb_decode(&istream, UARTMessage_fields, &uart_message);
    
	Service s = uart_message.service;
	
	// CREATING A NEW SERVICE OBJECT
    Service service = {0};
	service.serviceType=Service_ServiceType_SENSOR;
	service.serviceGroupId=1;
	service.serviceId=3;
	service.has_value=1;
	service.value=22;
	service.has_info=true;
	strcpy(service.info, "degree celcius");
	
	// WRAPPING SERVICE OBJECT IN A NEW UART MESSAGE
	UARTMessage u;
	u.has_service=true;
	u.has_beacon=true;
	
	if(beacon_sent==1){
		u.service=service;
		u.type=UARTMessage_Type_SERVICE;
		u.has_beacon=false;
		beacon_sent=0;
	} else {
		u.beacon=my_beacon;
		u.type=UARTMessage_Type_BEACON;
		u.has_service=false;
		beacon_sent=1;
	}
	
	// ENCODING A MESSAGE
	/* Create a stream that will write to our buffer. */
	pb_ostream_t ostream = pb_ostream_from_buffer(encBuffer, sizeof(encBuffer));
	pb_encode(&ostream, UARTMessage_fields, &u);
	uint8_t size = ostream.bytes_written;
	
	// SENDING A MESSAGE
	HAL_WriteUsart(&usart,&size,1);
	HAL_WriteUsart(&usart,&encBuffer,size);
}

static void createBeaconList(){
	my_beacon.serviceGroupId=1;
	my_beacon.has_name=true;
	strcpy(my_beacon.name,"living room");
	my_beacon.service_count=8;
	
	for(int i=0; i<8; i++){
		my_beacon.service[i].serviceGroupId=1;
		my_beacon.service[i].serviceId=i;
		my_beacon.service[i].has_value=true;
		my_beacon.service[i].has_info=true;
	
		// assign random values
		if(i<5){	
			my_beacon.service[i].serviceType=Service_ServiceType_SENSOR;
			my_beacon.service[i].value=rand() % 100;
			switch(i){
				case 0: strcpy(my_beacon.service[i].info, "temperature"); break; 
				case 1: strcpy(my_beacon.service[i].info, "light"); break;
				case 2: strcpy(my_beacon.service[i].info, "humidity"); break;
				case 3: strcpy(my_beacon.service[i].info, "pressure"); break;	
			}
		} else {
			my_beacon.service[i].serviceType=Service_ServiceType_ACTUATOR;
			my_beacon.service[i].value=i%2;
			switch(i){
				case 4: strcpy(my_beacon.service[i].info, "heater"); break; 
				case 5: strcpy(my_beacon.service[i].info, "light bulb"); break;
				case 6: strcpy(my_beacon.service[i].info, "radio"); break;
				case 7: strcpy(my_beacon.service[i].info, "television"); break;	
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
}