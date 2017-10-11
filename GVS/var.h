#ifndef __VARIABLE_H
#define __VARIABLE_H


/// Network socket parameters
#define ADDR "0.0.0.0"   // listening address (all network interfaces)
#define PORT 2000        // listening port


/// Buffer, page and struct size
#define page_size   0x1000				//4096kByte
#define BUF_SIZE (page_size + 256)      // buffer size
#define PAGE_REQ_SIZE sizeof(page_req_t)
#define PAGE_RESP_SIZE sizeof(page_resp_t)


/// Number of pages and base address
#define page_num        0x03
void *page_mem;

/// Socket file descriptor
int fd;

/// Page request modes
typedef enum {
	READ_REQ      = 0x00,
	WRITE_REQ     = 0x01
} page_mode_t;

/// Network commands
#define NET_CMD_SIZE     0x01
typedef enum {
  PAGE_REQ      = 0x00,
  PAGE_RESP     = 0x01,
  PAGE_PUSH     = 0x02
} net_cmd_t;

/**
 * network payload declaration
 */
#pragma pack(1)
typedef struct {
	unsigned char net_cmd;
	unsigned int id;                      ///< id of requested page
	unsigned int mode;                    ///< mode identifying whether page is requested for read or write
} page_req_t;
#pragma pack()

#pragma pack(1)
typedef struct {
	unsigned char net_cmd;
	unsigned int id;                      ///< id of requested page
	unsigned char data[page_size];        ///< page data
} page_resp_t;
#pragma pack()

// Prototype decalrations
void request_page(int id, page_mode_t mode);
void sync();

#endif /* __VARIABLE_H */
