/* Automatically generated nanopb constant definitions */
/* Generated by nanopb-0.2.7 at Sat Jun 28 11:24:44 2014. */

#include "uartmessage.pb.h"



const pb_field_t UARTMessage_fields[4] = {
    PB_FIELD2(  1, ENUM    , REQUIRED, STATIC  , FIRST, UARTMessage, type, type, 0),
    PB_FIELD2(  2, MESSAGE , OPTIONAL, STATIC  , OTHER, UARTMessage, beacon, type, &Beacon_fields),
    PB_FIELD2(  3, MESSAGE , OPTIONAL, STATIC  , OTHER, UARTMessage, service, beacon, &Service_fields),
    PB_LAST_FIELD
};

const pb_field_t Service_fields[6] = {
    PB_FIELD2(  1, ENUM    , REQUIRED, STATIC  , FIRST, Service, serviceType, serviceType, 0),
    PB_FIELD2(  2, UINT32  , REQUIRED, STATIC  , OTHER, Service, serviceGroupId, serviceType, 0),
    PB_FIELD2(  3, UINT32  , REQUIRED, STATIC  , OTHER, Service, serviceId, serviceGroupId, 0),
    PB_FIELD2(  4, UINT32  , OPTIONAL, STATIC  , OTHER, Service, value, serviceId, 0),
    PB_FIELD2(  5, STRING  , OPTIONAL, STATIC  , OTHER, Service, info, value, 0),
    PB_LAST_FIELD
};

const pb_field_t Beacon_fields[4] = {
    PB_FIELD2(  1, UINT32  , REQUIRED, STATIC  , FIRST, Beacon, serviceGroupId, serviceGroupId, 0),
    PB_FIELD2(  2, STRING  , OPTIONAL, STATIC  , OTHER, Beacon, name, serviceGroupId, 0),
    PB_FIELD2(  3, MESSAGE , REPEATED, STATIC  , OTHER, Beacon, services, name, &Service_fields),
    PB_LAST_FIELD
};


/* Check that field information fits in pb_field_t */
#if !defined(PB_FIELD_32BIT)
/* If you get an error here, it means that you need to define PB_FIELD_32BIT
 * compile-time option. You can do that in pb.h or on compiler command line.
 * 
 * The reason you need to do this is that some of your messages contain tag
 * numbers or field sizes that are larger than what can fit in 8 or 16 bit
 * field descriptors.
 */
STATIC_ASSERT((pb_membersize(UARTMessage, beacon) < 65536 && pb_membersize(UARTMessage, service) < 65536 && pb_membersize(Beacon, services[0]) < 65536), YOU_MUST_DEFINE_PB_FIELD_32BIT_FOR_MESSAGES_UARTMessage_Service_Beacon)
#endif

#if !defined(PB_FIELD_16BIT) && !defined(PB_FIELD_32BIT)
/* If you get an error here, it means that you need to define PB_FIELD_16BIT
 * compile-time option. You can do that in pb.h or on compiler command line.
 * 
 * The reason you need to do this is that some of your messages contain tag
 * numbers or field sizes that are larger than what can fit in the default
 * 8 bit descriptors.
 */
STATIC_ASSERT((pb_membersize(UARTMessage, beacon) < 256 && pb_membersize(UARTMessage, service) < 256 && pb_membersize(Beacon, services[0]) < 256), YOU_MUST_DEFINE_PB_FIELD_16BIT_FOR_MESSAGES_UARTMessage_Service_Beacon)
#endif


