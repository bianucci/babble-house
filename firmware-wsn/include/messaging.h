#ifndef MESSAGING_H
#define MESSAGING_H

#include <pb.h>
#include <usart.h>
#include <pb_encode.h>
#include <pb_decode.h>
#include <uartmessage.pb.h>
#include <app.h>

#define RX_BUFFER_SIZE 200
#define TX_BUFFER_SIZE 200

extern Beacon my_beacon;
extern Service my_serivce;
extern UARTMessage globalMessage;
extern uint8_t global_dst;
extern uint8_t messageRerceived_length;
extern HAL_UsartDescriptor_t usart;

void forwardMessageToPC();
void createBeaconList();
void usartRcvd(uint8_t size);
void usart_Init();
void sendUart(uint8_t* string, uint8_t size);
void assembleUartMessage(uint8_t serviceIndex);

#endif