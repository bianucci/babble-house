/* Automatically generated nanopb header */
/* Generated by nanopb-0.2.7 at Sat May 31 16:24:07 2014. */

#ifndef _PB_UARTMESSAGE_PB_H_
#define _PB_UARTMESSAGE_PB_H_
#include <pb.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Enum definitions */
typedef enum _UARTMessage_Type {
    UARTMessage_Type_SERVICE = 1,
    UARTMessage_Type_BEACON = 2
} UARTMessage_Type;

typedef enum _Service_ServiceType {
    Service_ServiceType_ACTUATOR = 1,
    Service_ServiceType_SENSOR = 2
} Service_ServiceType;

/* Struct definitions */
typedef struct _Service {
    Service_ServiceType serviceType;
    uint32_t serviceGroupId;
    uint32_t serviceId;
    bool has_value;
    uint32_t value;
    bool has_info;
    char info[32];
} Service;

typedef struct _Beacon {
    uint32_t serviceGroupId;
    bool has_name;
    char name[32];
    size_t service_count;
    Service service[10];
} Beacon;

typedef struct _UARTMessage {
    UARTMessage_Type type;
    bool has_beacon;
    Beacon beacon;
    bool has_service;
    Service service;
} UARTMessage;

/* Default values for struct fields */

/* Field tags (for use in manual encoding/decoding) */
#define Service_serviceType_tag                  1
#define Service_serviceGroupId_tag               2
#define Service_serviceId_tag                    3
#define Service_value_tag                        4
#define Service_info_tag                         5
#define Beacon_serviceGroupId_tag                1
#define Beacon_name_tag                          2
#define Beacon_service_tag                       3
#define UARTMessage_type_tag                     1
#define UARTMessage_beacon_tag                   2
#define UARTMessage_service_tag                  3

/* Struct field encoding specification for nanopb */
extern const pb_field_t UARTMessage_fields[4];
extern const pb_field_t Service_fields[6];
extern const pb_field_t Beacon_fields[4];

/* Maximum encoded size of messages (where known) */
#define UARTMessage_size                         709
#define Service_size                             58
#define Beacon_size                              640

#ifdef __cplusplus
} /* extern "C" */
#endif

#endif
