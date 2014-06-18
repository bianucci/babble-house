#include <netting.h>

ZDO_StartNetworkReq_t networkParams;
APS_DataReq_t dataReq;
AppMessage_t transmitData;
APS_RegisterEndpointReq_t endPoint;
SimpleDescriptor_t simpleDescriptor;

static uint8_t encBuffer[200];

void APS_DataConf(APS_DataConf_t* confInfo){
	//HAL_WriteUsart(&usart, "SENT_DATA\r\n", sizeof("SENT_DATA\r\n"));
}

void initTransmitData(void){
	dataReq.profileId=1;
	dataReq.dstAddrMode=APS_SHORT_ADDRESS;
	dataReq.dstAddress.shortAddress=CPU_TO_LE16(0);
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

void APS_DataInd(APS_DataInd_t *indData){
	uint8_t received = indData->asdu[0];
	//HAL_WriteUsart(&usart, "RCVD_DATA\r\n", sizeof("RCVD_DATA\r\n"));
}

void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo){
	uint8_t deviceType;
	CS_ReadParameter(CS_DEVICE_TYPE_ID, &deviceType);
	uint8_t status = confirmInfo->status;
	SYS_PostTask(APL_TASK_ID);
}

void startNetwork(){
	networkParams.ZDO_StartNetworkConf=ZDO_StartNetworkConf;
	ZDO_StartNetworkReq(&networkParams);
}