#ifndef NETTING_H
#define NETTING_H

#include <zdo.h>
#include <pb.h>
#include <pb_encode.h>
#include <pb_decode.h>
#include <uartmessage.pb.h>


BEGIN_PACK
typedef struct _AppMessage_t{
	uint8_t header[APS_ASDU_OFFSET];
	UARTMessage *message;
	uint8_t footer[APS_AFFIX_LENGTH - APS_ASDU_OFFSET];
} PACK AppMessage_t;
END_PACK

extern ZDO_StartNetworkReq_t networkParams;
extern APS_DataReq_t dataReq;
extern AppMessage_t transmitData;
extern APS_RegisterEndpointReq_t endPoint;
extern SimpleDescriptor_t simpleDescriptor;


void ZDO_StartNetworkConf(ZDO_StartNetworkConf_t *confirmInfo);
void initTransmitData(void);
void initEndpoint(void);
void APS_DataInd(APS_DataInd_t *indData);
void startNetwork();

#endif