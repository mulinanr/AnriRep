#ifndef __SOCKET_SETUP_H
#define __SOCKET_SETUP_H
// Network socket parameters
#define ADDR "0.0.0.0"   // listening address (all network interfaces)
#define PORT 2000        // listening port

// Socket initialization mode
typedef enum {
	CLIENT        = 0x00,
	SERVER        = 0x01
} socket_mode_t;

// Prototype declarations
int setup_socket(socket_mode_t mode);
#endif //__SOCKET_SETUP_H
