#include <netting.h>

ZDO_StartNetworkReq_t networkParams;
APS_DataReq_t dataReq;
AppMessage_t transmitData;
APS_RegisterEndpointReq_t endPoint;
SimpleDescriptor_t simpleDescriptor;

void APS_DataConf(APS_DataConf_t* confInfo){
	if(log_enabled){
		if(confInfo->status==APS_SUCCESS_STATUS){
			sendUart((uint8_t*)"ZBSENT\n\r", sizeof("ZBSENT\n\r"));
		}else{
			sendUart((uint8_t*)"ZSSENDF\n\r", sizeof("ZSSENDF\n\r"));
		}
	}
	SYS_PostTask(APL_TASK_ID);
}

void initTransmitData(uint8_t receiver){
	dataReq.profileId=1;
	dataReq.dstAddrMode=APS_SHORT_ADDRESS;
	dataReq.dstAddress.shortAddress=CPU_TO_LE16(receiver);
	dataReq.dstEndpoint=1;
	dataReq.asdu=transmitData.data;
	dataReq.asduLength=sizeof(transmitData.data);
	dataReq.srcEndpoint=1;
	dataReq.APS_DataConf=APS_DataConf;
}

void initEndpoint(void){
	simpleDescriptor.AppDeviceId = 1;
	simpleDescriptor.AppProfileId = 1;
	simpleDescriptor.endpoint = 1;
	simpleDescriptor.AppDeviceVersion = 1;
	endPoint.simpleDescriptor = &simpleDescriptor;
	endPoint.APS_DataInd = APS_DataInd;
	APS_RegisterEndpointReq(&endPoint);
}

void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo){
	uint8_t deviceType;
	CS_ReadParameter(CS_DEVICE_TYPE_ID, &deviceType);
	uint8_t network_status = confirmInfo->status;
	if(log_enabled){
		if(ZDO_SUCCESS_STATUS==network_status){
			HAL_WriteUsart(&usart, (uint8_t*)"JND_NW\n\r", sizeof("JND_NW\n\r"));
		} else {
			HAL_WriteUsart(&usart, (uint8_t*)"NO_NW\n\r", sizeof("NO_NW\n\r"));
		}
	}
	SYS_PostTask(APL_TASK_ID);
}

void startNetwork(){
	networkParams.ZDO_StartNetworkConf=ZDO_StartNetworkConf;
	ZDO_StartNetworkReq(&networkParams);
}

uint8_t nextMessageLength;
void APS_DataInd(APS_DataInd_t *indData){
	if(log_enabled){sendUart((uint8_t*)"DATA_IN\n\r", sizeof("DATA_IN\n\r"));}
	messageRerceived_length=indData->asdu[79];
	pb_istream_t istream = pb_istream_from_buffer(indData->asdu, messageRerceived_length);
	bool status = pb_decode(&istream, UARTMessage_fields, &globalMessage);
	if(status){
		wakeUpZigBeeReceived();
	}
}

void sendProtoViaZigBee(UARTMessage* message){
	pb_ostream_t ostream = pb_ostream_from_buffer(transmitData.data, sizeof(transmitData.data));
	pb_encode(&ostream, UARTMessage_fields, message);
	uint8_t size = ostream.bytes_written;
	transmitData.data[79]=size;
	
	uint8_t receiver=0;
	
	#if CS_DEVICE_TYPE==DEV_TYPE_COORDINATOR
		receiver = (uint8_t) message->service.serviceGroupId;
	#else
		receiver = 0; // receiver will be the coordinator
	#endif
	
	initTransmitData(receiver);
	APS_DataReq(&dataReq);
}