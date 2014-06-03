/**************************************************************************//**
 
  \file myusart.c
 
  \application to read data from USART.
 
  \the application turns on the Red light, if 'a' is received, Green Light if 
  \'b' is received and Yellow light for anything else
 
******************************************************************************/
 
#include <appTimer.h>
#include <zdo.h>
#include <app.h>
#include <usart.h>
 
#define RX_BUFFER_SIZE 200
#define TX_BUFFER_SIZE 200
 
static HAL_UsartDescriptor_t usart;
static HAL_AppTimer_t sendTimer;    

// ZIGBEE BEGIN
static AppState_t appState = APP_INIT_STATE;
static uint8_t deviceType;
static void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo);
static ZDO_StartNetworkReq_t networkParams;
static void printNetworkStatus(void);
// ZIGBEE END
 
static uint8_t Rx_Buffer[RX_BUFFER_SIZE];
static uint8_t Tx_Buffer[TX_BUFFER_SIZE];
static uint8_t buffer[RX_BUFFER_SIZE];
static void printMessageFired(void);

static void createBeaconList();
static Beacon my_beacon;

static int buffer_counter = 0;
static uint8_t message_length = 0;

static uint8_t last_msg[100]; 
static uint8_t last_msg_length=0;

static uint8_t beacon_sent=0;

BEGIN_PACK
typedef struct _AppMessage_t{
	uint8_t header[APS_ASDU_OFFSET];
	uint8_t data[9];
	uint8_t footer[APS_AFFIX_LENGTH - APS_ASDU_OFFSET];
} PACK AppMessage_t;
END_PACK

static AppMessage_t transmitData;
APS_DataReq_t dataReq;
static void APS_DataConf(APS_DataConf_t *confInfo);
static void initTransmitData(void);

static SimpleDescriptor_t simpleDescriptor;
static APS_RegisterEndpointReq_t endPoint;
static void initEndpoint (void);
void APS_DataInd(APS_DataInd_t *indData);

HAL_AppTimer_t receiveTimerLed;
HAL_AppTimer_t transmitTimerLed;
HAL_AppTimer_t transmitTimer;
static void receiveTimerLedFired(void);
static void transmitTimerLedFired(void);
static void transmitTimerFired(void);
static void initTimer(void);

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

static uint8_t uninitialized = 2;

void APL_TaskHandler(void)
{
	if(uninitialized==1){
		usart_Init();
		HAL_OpenUsart(&usart);
	
		srand(42);
		createBeaconList();
		
		sendTimer.interval = 1000;
		sendTimer.mode = TIMER_REPEAT_MODE;
		sendTimer.callback = printMessageFired;
		HAL_StartAppTimer(&sendTimer);
		
		uninitialized=0;
	}
	
	switch(appState){
		case APP_INIT_STATE:
			usart_Init();
			HAL_OpenUsart(&usart);
			appState=APP_START_NETWORK_STATE;
			SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_START_NETWORK_STATE:
			networkParams.ZDO_StartNetworkConf=ZDO_StartNetworkConf;
			ZDO_StartNetworkReq(&networkParams);
			appState=APP_INIT_ENDPOINT_STATE;
			break;
			
		case APP_INIT_ENDPOINT_STATE:
			initEndpoint();
			#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
				appState=APP_NOTHING_STATE;
			#else
				appState=APP_INIT_TRANSMITDATA_STATE;
			#endif;
			SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_INIT_TRANSMITDATA_STATE: 		
			initTransmitData();
			appState=APP_NOTHING_STATE;
			HAL_StartAppTimer(&transmitTimer);
			SYS_PostTask(APL_TASK_ID);
			break;
			
		case APP_TRANSMIT_STATE:
			#if CS_DEVICE_TYPE==DEV_TYPE_ENDDEVICE
				transmitData.data[0]='H'; transmitData.data[0]='a'; transmitData.data[0]='l'; 
				transmitData.data[0]='l'; transmitData.data[0]='o'; transmitData.data[0]=' ';
			#else
				transmitData.data[0]='Z'; transmitData.data[0]='i'; transmitData.data[0]='g';
				transmitData.data[0]='B'; transmitData.data[0]='e'; transmitData.data[0]='e';
			#endif;
			APS_DataReq(&dataReq);
			break;
		
		case APP_NOTHING_STATE:
			break;
	}
}

static void initEndpoint(void){
	simpleDescriptor.AppDeviceId = 1;
	simpleDescriptor.AppProfileId = 1;
	simpleDescriptor.endpoint = 1;
	simpleDescriptor.AppDeviceVersion = 1;
	endPoint.simpleDescriptor = &simpleDescriptor;
	endPoint.APS_DataInd = APS_DataInd;
	APS_RegisterEndpointReq(&endPoint);
}

static void initTimer(void){
	transmitTimerLed.interval=500;
	transmitTimerLed.mode=TIMER_ONE_SHOT_MODE;
	transmitTimerLed.callback=transmitTimerLedFired;
	
	receiveTimerLed.interval=500;
	receiveTimerLed.mode=TIMER_ONE_SHOT_MODE;
	receiveTimerLed.callback=receiveTimerLedFired;

	transmitTimer.interval=3000;
	transmitTimer.mode=TIMER_REPEAT_MODE;
	transmitTimer.callback=transmitTimerFired;
}

static void initTransmitData(){
	dataReq.profileId=1;
	dataReq.dstAddrMode=APS_SHORT_ADDRESS;
	dataReq.dstAddress.shortAddress=CPU_TO_LE16(0);
	dataReq.dstEndpoint=1;
	dataReq.asdu=transmitData.data;
	dataReq.asduLength=sizeof(transmitData.data);
	dataReq.srcEndpoint=1;
	dataReq.APS_DataConf=APS_DataConf;
}

static void transmitTimerLedFired(void){
}

static void receiveTimerLedFired(void){
}

static void transmitTimerFired(void){
	appState=APP_TRANSMIT_STATE;
	SYS_PostTaks(APL_TASK_ID);
}

void APS_DataInd(APS_DataInd_t *indData){
	HAL_WriteUsart(&usart, "RCVD_DATA\r\n", sizeof("RCVD_DATA\r\n"));
	//HAL_StartAppTimer(&receiveTimerLed);
	HAL_WriteUsart(&usart, indData->asdu, indData->asduLength);
}

static void APS_DataConf(APS_DataConf_t *confInfo){
	HAL_WriteUsart(&usart, "SENT_DATA\r\n", sizeof("SENT_DATA\r\n"));
	//HAL_StartAppTimer(&transmitTimerLed);
	appState=APP_NOTHING_STATE;
	SYS_PostTask(APL_TASK_ID);
}

static uint8_t network_status=0;
void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo){
	CS_ReadParameter(CS_DEVICE_TYPE_ID, &deviceType);
	if(ZDO_SUCCESS_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_SUCCESS_STATUS", sizeof("ZDO_SUCCESS_STATUS"));}
	if(ZDO_INVALID_REQUEST_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_INVALID_REQUEST_STATUS", sizeof("ZDO_INVALID_REQUEST_STATUS"));}
	if(ZDO_STATIC_ADDRESS_CONFLICT_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_STATIC_ADDRESS_CONFLICT_STATUS", sizeof("ZDO_STATIC_ADDRESS_CONFLICT_STATUS"));}
	if(ZDO_USER_DESCRIPTOR_UPDATE_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_USER_DESCRIPTOR_UPDATE_STATUS", sizeof("ZDO_USER_DESCRIPTOR_UPDATE_STATUS"));}
		
	SYS_PostTask(APL_TASK_ID);
}

static void printNetworkStatus(){
	if(ZDO_SUCCESS_STATUS==network_status){
		//HAL_WriteUsart(&usart, "JOINED DUDE\r\n", sizeof("JOINED DUDE\r\n"));
	} else {
		//HAL_WriteUsart(&usart, "FAILED DUDE\r\n", sizeof("FAILED DUDE\r\n"));
		//HAL_WriteUsart(&usart, network_status, 1);
	}
}

static uint8_t encBuffer[200];
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