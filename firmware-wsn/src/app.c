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
 
static uint8_t Rx_Buffer[RX_BUFFER_SIZE];
static uint8_t Tx_Buffer[TX_BUFFER_SIZE];
static uint8_t buffer[RX_BUFFER_SIZE];
static void printMessageFired(void);

static int buffer_counter = 0;
static uint8_t message_length = 0;

static uint8_t last_msg[100]; 
static uint8_t last_msg_length=0;

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

static uint8_t uninitialized = 1;

void APL_TaskHandler(void)
{
	if(uninitialized==1){
		usart_Init();
		HAL_OpenUsart(&usart);
	
		sendTimer.interval = 1000;
		sendTimer.mode = TIMER_REPEAT_MODE;
		sendTimer.callback = printMessageFired;
		HAL_StartAppTimer(&sendTimer);
		
		uninitialized=0;
	}
}

static uint8_t encBuffer[100];

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
	u.type=UARTMessage_Type_SERVICE;
	u.has_service=true;
	u.service=service;
	
	// ENCODING A MESSAGE
	/* Create a stream that will write to our buffer. */
	pb_ostream_t ostream = pb_ostream_from_buffer(encBuffer, sizeof(encBuffer));
	pb_encode(&ostream, UARTMessage_fields, &u);
	uint8_t size = ostream.bytes_written;
	
	// SENDING A MESSAGE
	HAL_WriteUsart(&usart,&size,1);
	HAL_WriteUsart(&usart,&encBuffer,size);
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