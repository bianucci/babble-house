#include <netting.h>

ZDO_StartNetworkReq_t networkParams;
APS_DataReq_t dataReq;
AppMessage_t transmitData;
APS_RegisterEndpointReq_t endPoint;
SimpleDescriptor_t simpleDescriptor;

uint8_t encBuffer[200];

void APS_DataConf(APS_DataConf_t* confInfo){
	if(log_enabled){sendUart((uint8_t*)"SENT_DATA\n\r", sizeof("SENT_DATA\n\r"));}
}

void initTransmitData(void){
	dataReq.profileId=1;
	dataReq.dstAddrMode=APS_SHORT_ADDRESS;
	dataReq.dstAddress.shortAddress=CPU_TO_LE16(0);
	dataReq.dstEndpoint=1;
	dataReq.asdu=transmitData.message;
	dataReq.asduLength=sizeof(transmitData.message);
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

void APS_DataInd(APS_DataInd_t *indData){
	if(log_enabled){sendUart((uint8_t*)"DATA_IN\n\r", sizeof("DATA_IN\n\r"));}
	messageReceived=&indData->asdu;
}

void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo){
	uint8_t deviceType;
	CS_ReadParameter(CS_DEVICE_TYPE_ID, &deviceType);
	uint8_t network_status = confirmInfo->status;
	if(log_enabled){
		if(ZDO_SUCCESS_STATUS==network_status){
			HAL_WriteUsart(&usart, "JOINED NETWORK\n\r", sizeof("JOINED NETWORK\n\r"));
		} else {
			HAL_WriteUsart(&usart, "NO NETWORK\n\r", sizeof("NO NETWORK\n\r"));
		}
	}
	SYS_PostTask(APL_TASK_ID);
}

void startNetwork(){
	networkParams.ZDO_StartNetworkConf=ZDO_StartNetworkConf;
	ZDO_StartNetworkReq(&networkParams);
}

void send_uart_as_zigbee(UARTMessage* message){
	initTransmitData();
	transmitData.message=message;
	APS_DataReq(&dataReq);
}