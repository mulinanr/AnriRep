#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

#include "var.h"
#include "signal_handler.h"
#include "client.h"

void *client_recv(void *vfd) {
	int fd = (size_t) vfd;
	char buf[BUF_SIZE];
	int rbytes;

	page_req_t *req_buf = (page_req_t *) buf;
	page_resp_t *resp_buf = (page_resp_t *) buf;

	printf("[New thread created (fd: %i)]\n", fd);

	// Receive net commands in endless loop
	while (1) {
		// Receive only first byte (net command)
		if ((rbytes = recv(fd, &req_buf->net_cmd, NET_CMD_SIZE, MSG_WAITALL))
				<= 0)
			break;

		// receive further payload depending on received network command
		switch (req_buf->net_cmd) {
		case PAGE_RESP:   // receive page response payload
			pthread_mutex_lock(&sync_page_req_mutex);
			if ((rbytes = recv(fd, &resp_buf->id,
					(PAGE_RESP_SIZE - NET_CMD_SIZE), MSG_WAITALL)) <= 0)
				break;
			printf("Page response received (id: %u)\n", req_buf->id);
			mprotect((page_mem + (resp_buf->id * page_size)), page_size,
					PROT_WRITE);
			memcpy((page_mem + (resp_buf->id * page_size)), resp_buf->data,
					page_size); // store page content (needs temporary write access)
			mprotect((page_mem + (resp_buf->id * page_size)), page_size,
					PROT_NONE);

			pthread_cond_signal(&sync_page_req_cond); // trigger condition variable regarding blocking page request
			pthread_mutex_unlock(&sync_page_req_mutex);
			break;
		default:
			fprintf(stderr,
					"Invalid network command received. Terminating application\n");
			exit(EXIT_FAILURE);
		}
	}

	close(fd);
	printf("[Thread terminated (fd: %i)]\n", fd);
	pthread_exit(NULL);
}

/**
 * Request memory page from server
 *  - Called by client
 *  - Synchronous request/reply communication within one thread
 * @param[in] id Page id
 * @param[in] mode Request mode (read/write)
 */
void request_page(int id, page_mode_t mode){
	char buf[BUF_SIZE];                                                // temporary buffer for network packets
	page_req_t *req_buf = (page_req_t *)buf;                           // use buffer as data structure

	// Send page request command
	req_buf->net_cmd = PAGE_REQ;
	req_buf->id = id;
	req_buf->mode = mode;

	pthread_mutex_lock(&sync_page_req_mutex);
	if (send(fd, req_buf, PAGE_REQ_SIZE, 0) == -1) {
		perror("send() failed");
		exit(EXIT_FAILURE);
	}
	pthread_cond_wait(&sync_page_req_cond, &sync_page_req_mutex);      // wait until response received
	pthread_mutex_unlock(&sync_page_req_mutex);
}


/**
 * Send written memory pages back to server
 *  - Called by client
 *  - Asynchronous communication (awaits no response) within one thread
 */
void sync()
{
	char buf[BUF_SIZE];                                                // temporary buffer for network packets
	void *addr;                                                        // address
	int i;

	page_resp_t *resp_buf = (page_resp_t *)buf;                        // use buffer as data structure

	// Sync written pages
	for (i = 0; i < page_num; i++) {
		addr = page_mem + (i * page_size);
		if (get_page_state(i) == PAGE_WRITE) {
			printf("Syncing memory page %p (id: %i)\n", addr, i);

			// Send page push command (releases write lock on server)
			resp_buf->net_cmd = PAGE_PUSH;
			resp_buf->id = i;
			memcpy(resp_buf->data, addr, page_size);
			if (send(fd, resp_buf, PAGE_RESP_SIZE, 0) == -1) {
				perror("send() failed");
				exit(EXIT_FAILURE);
			}

			// Reset page state and revoke access rights
			set_page_state(i, PAGE_NOACC);
			mprotect(addr, page_size, PROT_NONE);
		}
	}
}
