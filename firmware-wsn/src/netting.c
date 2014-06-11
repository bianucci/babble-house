static uint8_t deviceType;
static void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo);
static ZDO_StartNetworkReq_t networkParams;
static void printNetworkStatus(void);

static uint8_t last_msg[100]; 
static uint8_t last_msg_length=0;

BEGIN_PACK
typedef struct _AppMessage_t{
	uint8_t header[APS_ASDU_OFFSET];
	uint8_t data[1];
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

static void initEndpoint(void){
	simpleDescriptor.AppDeviceId = 1;
	simpleDescriptor.AppProfileId = 1;
	simpleDescriptor.endpoint = 1;
	simpleDescriptor.AppDeviceVersion = 1;
	endPoint.simpleDescriptor = &simpleDescriptor;
	endPoint.APS_DataInd = APS_DataInd;
	APS_RegisterEndpointReq(&endPoint);
}

static void APS_DataConf(APS_DataConf_t *confInfo){
	//HAL_WriteUsart(&usart, "SENT_DATA\r\n", sizeof("SENT_DATA\r\n"));
	//HAL_StartAppTimer(&transmitTimerLed);
	appState=APP_NOTHING_STATE;
	SYS_PostTask(APL_TASK_ID);
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

static uint8_t encBuffer[200];
void APS_DataInd(APS_DataInd_t *indData){
	// CREATING A NEW SERVICE OBJECT
    Service service = {0};
	service.serviceType=Service_ServiceType_SENSOR;
	service.serviceGroupId=1;
	service.serviceId=3;
	service.has_value=true;
	service.value=indData->asdu[0];
	service.has_info=true;
	strcpy(service.info, "light");
	
	// WRAPPING SERVICE OBJECT IN A NEW UART MESSAGE
	UARTMessage u;
	u.has_service=true;
	u.service=service;
	u.type=UARTMessage_Type_SERVICE;
	u.has_beacon=false;
	
	// ENCODING A MESSAGE
	/* Create a stream that will write to our buffer. */
	pb_ostream_t ostream = pb_ostream_from_buffer(encBuffer, sizeof(encBuffer));
	pb_encode(&ostream, UARTMessage_fields, &u);
	uint8_t size = ostream.bytes_written;
	
	// SENDING A MESSAGE
	HAL_WriteUsart(&usart,&size,1);
	HAL_WriteUsart(&usart,&encBuffer,size);
}

static uint8_t network_status=0;
void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo){
	CS_ReadParameter(CS_DEVICE_TYPE_ID, &deviceType);
	//if(ZDO_SUCCESS_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_SUCCESS_STATUS", sizeof("ZDO_SUCCESS_STATUS"));}
	//if(ZDO_INVALID_REQUEST_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_INVALID_REQUEST_STATUS", sizeof("ZDO_INVALID_REQUEST_STATUS"));}
	//if(ZDO_STATIC_ADDRESS_CONFLICT_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_STATIC_ADDRESS_CONFLICT_STATUS", sizeof("ZDO_STATIC_ADDRESS_CONFLICT_STATUS"));}
	//if(ZDO_USER_DESCRIPTOR_UPDATE_STATUS==confirmInfo->status){HAL_WriteUsart(&usart, "ZDO_USER_DESCRIPTOR_UPDATE_STATUS", sizeof("ZDO_USER_DESCRIPTOR_UPDATE_STATUS"));}
		
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

