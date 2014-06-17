#ifndef MESSAGING_H
#define MESSAGING_H

#include <pb.h>
#include <usart.h>
#include <pb_encode.h>
#include <pb_decode.h>
#include <uartmessage.pb.h>

#define RX_BUFFER_SIZE 200
#define TX_BUFFER_SIZE 200

extern Beacon my_beacon;
extern HAL_UsartDescriptor_t usart;

void createBeaconList();
void usartRcvd(uint8_t size);
void usart_Init();

#endif MESSAGING_H