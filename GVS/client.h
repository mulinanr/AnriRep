#ifndef CLIENT_H
#define CLIENT_H

#include "var.h"

pthread_mutex_t sync_page_req_mutex;
pthread_cond_t sync_page_req_cond;

/// Printing string length
#define STR_LEN 10

/// Random client operations
typedef enum {
  READ      = 0x00,
  WRITE     = 0x01,
  SYNC      = 0x02
} client_operations_t;

// Prototype decalrations
void request_page(int id, page_mode_t mode);
void *client_recv(void *vfd);
void sync();

#endif /* __PAGE_SERVER_CLIENT_H */
