#ifndef _CONFIGURATION_H_
#define _CONFIGURATION_H_

#define APP_DISABLE_BSP 0
#include <BoardConfig.h>
#define APP_INTERFACE_USART 0x01
#define APP_INTERFACE_VCP 0x02
#define APP_INTERFACE_SPI 0x03
#define APP_INTERFACE_UART 0x04
#define APP_INTERFACE_USBFIFO 0x05
#define AT25F2048  0x01
#define AT45DB041  0x02
#define AT25DF041A 0x03
#define APP_FRAGMENTATION 0
#define APP_USE_BINDING 0
#define APP_BLINK_PERIOD 1000
#define APP_MIN_BLINK_PERIOD 100
#define APP_MAX_BLINK_PERIOD 10000
#define APP_USE_APS_ACK 0
#ifdef BOARD_MESHBEAN
  #define BSP_MNZB_EVB_SUPPORT 0
  #define APP_INTERFACE APP_INTERFACE_USART
  #define APP_USART_CHANNEL USART_CHANNEL_1
#endif
#ifdef BOARD_RCB
  #define BSP_ENABLE_RS232_CONTROL 1
  #define APP_INTERFACE APP_INTERFACE_USART
  #if (APP_INTERFACE == APP_INTERFACE_USART)
    #define APP_USART_CHANNEL USART_CHANNEL_1
  #endif
  #if (APP_INTERFACE == APP_INTERFACE_USBFIFO)
    #undef APP_USART_CHANNEL
    #define APP_USART_CHANNEL USART_CHANNEL_USBFIFO
  #endif
#endif
#define CS_ZDP_RESPONSE_TIMEOUT 0
#define NWK_CONCENTRATOR_DISCOVERY_TIME 20000UL
#define CS_ZCL_ATTRIBUTE_REPORT_TIMER_INTERVAL 4000

//-----------------------------------------------
//AT86RF212
//-----------------------------------------------
#ifdef AT86RF212
  // Enables or disables Listen Before Talk feature.
  #define CS_LBT_MODE false
  //  Valid channel numbers for 900 MHz band are 0x00 - 0x0a
  //  C-type: uint32_t
  #define CS_CHANNEL_MASK (1L<<0x00)
  //  Value range:
  //  0 - 915MHz (BPSK-40, channels 0x01 - 0x0a), 868MHz (BPSK-20, channel 0x00)
  //  2 - 915MHz (O-QPSK-250, channels 0x01 - 0x0a), 868Mhz (O-QPSK-100, channel 0x00)
  //  5 - 780MHz (O-QPSK-250, channels 0x00 - 0x03, Chinese band)
  //  C-type: uint8_t
  #define CS_CHANNEL_PAGE 0
#else
  #define CS_CHANNEL_MASK (1L<<0x00)
#endif
// PAN ID
#define CS_EXT_PANID 0x5854484353545354LL

// Enables or disables use of predefined PAN ID. Actual PAN ID is specified via
// CS_NWK_PANID parameter. If predefined PAN ID is disabled then PAN ID is selected
// randomly (default).
#define CS_NWK_PREDEFINED_PANID false

// Short PAN ID of the network to start or to join to. The parameter holds the
// short PANID value generated randomly if CS_NWK_PREDEFINED_PANID equals false.
// Otherwise, the predefined parameter's value is used as the short PANID.
#define CS_NWK_PANID 0x0000

#define CS_INDIRECT_POLL_RATE 1000

#define CS_SCAN_DURATION 5
#define CS_ZDO_JOIN_ATTEMPTS 4
#define CS_ZDO_JOIN_INTERVAL 1000

//  If set to 1, the CS_NWK_ADDR parameter will be used as the device's short
// address. Otherwise, the short address is assigned automatically by the stack
//#define CS_NWK_UNIQUE_ADDR 0
#define CS_NWK_UNIQUE_ADDR 1

#if (CS_NWK_UNIQUE_ADDR == 1)
  // Specifies short (network) address if CS_NWK_UNIQUE_ADDR equals 1
  //  Note: the coordinator short address always equals 0x0000.
  //  Value range: 0x0000 - 0xFFF8
  #define CS_NWK_ADDR 0x0123
  //#define CS_NWK_ADDR 0x0000
#endif

//#define CS_DEVICE_TYPE DEVICE_TYPE_ROUTER
//#define CS_DEVICE_TYPE DEVICE_TYPE_COORDINATOR 
#define CS_DEVICE_TYPE DEV_TYPE_ENDDEVICE

// 64-bit Unique Identifier (UID) determining the device extended address.
//#define CS_UID 0x5854544245441001LL  
#define CS_UID 0x5854544245441231LL

#define CS_ADDRESS_ASSIGNMENT_METHOD 2
#define CS_USER_DESCRIPTOR_AVAILABLE true
#define CS_MAX_CHILDREN_AMOUNT 8
#define CS_MAX_CHILDREN_ROUTER_AMOUNT 2
#define CS_MAX_NETWORK_DEPTH 5
#define CS_PERMIT_DURATION 0xFF
#define CS_NWK_MAX_LINK_STATUS_FAILURES 3
#define CS_NWK_END_DEVICE_MAX_FAILURES 3
#define CS_RX_ON_WHEN_IDLE false
#define CS_END_DEVICE_SLEEP_PERIOD 10000L
#define CS_FFD_SLEEP_PERIOD 10000
#define CS_APS_KEY_PAIR_DESCRIPTORS_AMOUNT 4
#ifdef STANDARD_SECURITY_MODE
  #define CS_ZDO_SECURITY_STATUS 0
  #define CS_NETWORK_KEY {0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC}
  #define CS_NWK_SECURITY_KEYS_AMOUNT 1
  #define CS_APS_TRUST_CENTER_ADDRESS 0xAAAAAAAAAAAAAAAALL
  #define CS_APS_SECURITY_TIMEOUT_PERIOD 10000
#endif
#ifdef STDLINK_SECURITY_MODE
  #define CS_APS_TRUST_CENTER_ADDRESS 0xAAAAAAAAAAAAAAAALL
  #define CS_APS_SECURITY_TIMEOUT_PERIOD 10000
  #define CS_NETWORK_KEY {0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC,0xCC}
  #define CS_ZDO_SECURITY_STATUS 1
  #define CS_NWK_SECURITY_KEYS_AMOUNT 1
#endif
#define CS_MAX_TC_ALLOWED_DEVICES_AMOUNT 5
#define CS_MAX_TC_AUTHENTIC_PROCESS_AMOUNT 1
#define CS_ZDO_TC_KEEP_ALIVE_INTERVAL 20
#define CS_GROUP_TABLE_SIZE 1
#define CS_NEIB_TABLE_SIZE 8
#define CS_ROUTE_TABLE_SIZE 10
#define CS_ADDRESS_MAP_TABLE_SIZE 5
#define CS_ROUTE_DISCOVERY_TABLE_SIZE 3
#define CS_DUPLICATE_REJECTION_TABLE_SIZE 8
#define CS_NWK_BTT_SIZE 8
#define CS_APS_BINDING_TABLE_SIZE 1
#define CS_BAN_TABLE_SIZE 1
#define CS_ROUTE_CACHE_SIZE 1
#define CS_APS_DATA_REQ_BUFFERS_AMOUNT 3
#define CS_APS_ACK_FRAME_BUFFERS_AMOUNT 2
#define CS_NWK_BUFFERS_AMOUNT 4
#define CS_ZCL_MEMORY_BUFFERS_AMOUNT 5
#define CS_MAC_TRANSACTION_TIME 7680L
#define CS_APS_MAX_FRAME_RETRIES 3
#define CS_MAX_NEIGHBOR_ROUTE_COST 8
#define CS_NWK_USE_MULTICAST true
#define CS_RF_TX_POWER 3
#if (APP_FRAGMENTATION == 1)
  #define CS_APS_MAX_BLOCKS_AMOUNT 4
  #define CS_APS_BLOCK_SIZE 0
#endif
#define CS_APS_MAX_TRANSMISSION_WINDOW_SIZE 1
#if (APP_USE_OTAU == 1)
  #define APP_USE_FAKE_OFD_DRIVER 0
  #define EXTERNAL_MEMORY AT45DB041
  #define OTAU_CLIENT
  #define CS_ZCL_OTAU_DISCOVERED_SERVER_AMOUNT 1
  #define CS_ZCL_OTAU_CLIENT_SESSION_AMOUNT 1
  #define CS_ZCL_OTAU_SERVER_DISCOVERY_PERIOD 60000
  #define CS_ZCL_OTAU_DEFAULT_UPGRADE_SERVER_IEEE_ADDRESS 0xFFFFFFFFFFFFFFFFull
  #define CS_ZCL_OTAU_IMAGE_PAGE_REQUEST_USAGE 1
#endif //
#if (CS_ZCL_OTAU_IMAGE_PAGE_REQUEST_USAGE == 1)
  #define CS_ZCL_OTAU_IMAGE_PAGE_REQUEST_RESPONSE_SPACING 200
  #define CS_ZCL_OTAU_IMAGE_PAGE_REQUEST_PAGE_SIZE 256
#endif
#endif // _CONFIGURATION_H_
