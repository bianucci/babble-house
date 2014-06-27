#ifndef ACTUATING_H
#define ACTUATING_H

#include <app.h>
#include <usart.h>
#include <messaging.h>
#include <uartmessage.pb.h>

void handleServiceRequests(UARTMessage* request);
void changeStateActuatorOne(uint8_t newState);
void changeStateActuatorTwo(uint8_t newState);
void changeStateActuatorThree(uint8_t newState);
void initActuators();

#endif