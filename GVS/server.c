#include<stdio.h>
#include<string.h>    //strlen
#include<stdlib.h>    //strlen
#include<sys/socket.h>
#include<arpa/inet.h> //inet_addr
#include<unistd.h>    //write, close
#include<pthread.h> //for threading , link with lpthread

#include"var.h"
#include "sock_setup.h"

#define UNLOCKED   -1                                                       // unset write lock indicator in write lock array
unsigned int page_write_lock[page_num] = {UNLOCKED, UNLOCKED, UNLOCKED};   // file descriptor array which holds write locks for shared memory pages
pthread_mutex_t claim_wlock_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t claim_wlock_cond = PTHREAD_COND_INITIALIZER;


/**
 * Claims write lock for a page to allow clients to gain exclusive write access
 *  - Function blocks until an already granted lock is released
 * @param[in] id Page id
 * @param[in] fd File descriptor (unique client id)
 */
void claim_exclusive_write(int id, int fd)
{
	pthread_mutex_lock(&claim_wlock_mutex);
	while (1) {                                                        // wait for lock until granted
		if (page_write_lock[id] == UNLOCKED) {                           // check lock state
			page_write_lock[id] = fd;
			printf("Page locked (id: %i, fd: %i)\n", id, fd);
			pthread_mutex_unlock(&claim_wlock_mutex);
			break;
		} else {
			pthread_cond_wait(&claim_wlock_cond, &claim_wlock_mutex);      // block if lock is already assigned to another client
		}
	}
}


/**
 * Releases a page's write lock, hold by a client
 *  - Function releases blocking lock requests
 * @param[in] id Page id
 * @param[in] fd File descriptor (unique client id)
 */
void release_exclusive_write(int id, int fd)
{
	pthread_mutex_lock(&claim_wlock_mutex);
	if (page_write_lock[id] == fd) {                                   // check lock state
		page_write_lock[id] = UNLOCKED;
		printf("Page unlocked (id: %i, fd: %i)\n", id, fd);
		pthread_cond_signal(&claim_wlock_cond);                          // trigger condition variable regarding blocking lock requests
	}
	pthread_mutex_unlock(&claim_wlock_mutex);
}

void* server_recv(void *vfd){
	int fd = (size_t)vfd;
		char buf[BUF_SIZE];
		int rbytes;

		page_req_t *req_buf = (page_req_t *)buf;
		page_resp_t *resp_buf = (page_resp_t *)buf;

		printf("[New thread created (fd: %i)]\n", fd);

		// Receive net commands in endless loop
		while(1) {
			// Receive only first byte (net command)
			if ((rbytes = recv(fd, &req_buf->net_cmd, NET_CMD_SIZE, MSG_WAITALL)) <= 0) break;

			// receive further payload depending on received network command
			switch (req_buf->net_cmd) {
				case PAGE_REQ:   // receive page request payload
					if ((rbytes = recv(fd, &req_buf->id, (PAGE_REQ_SIZE - NET_CMD_SIZE), MSG_WAITALL)) <= 0) break;

					switch (req_buf->mode) {
						case READ_REQ:
							printf("Page read request received (id: %u, fd: %i)\n", req_buf->id, fd);
							break;
						case WRITE_REQ:
							printf("Page write request received (id: %u, fd: %i)\n", req_buf->id, fd);
							claim_exclusive_write(req_buf->id, fd);
							break;
						default:
							fprintf(stderr, "Invalid page mode in page request (id: %i, mode: %i, fd: %i). Terminating application.\n", req_buf->id, req_buf->mode, fd);
							exit(EXIT_FAILURE);
					}

					// Send page response
					resp_buf->net_cmd = PAGE_RESP;
					memcpy(resp_buf->data, (page_mem + (req_buf->id * page_size)), page_size);
					if (send(fd, resp_buf, PAGE_RESP_SIZE, 0) == -1) {
						perror("send() failed");
						exit(EXIT_FAILURE);
					}
					break;

				case PAGE_PUSH:   // Receive page content (sync() function)
					if ((rbytes = recv(fd, &resp_buf->id, (PAGE_RESP_SIZE - NET_CMD_SIZE), MSG_WAITALL)) <= 0) break;
					printf("Page push received (id: %u, fd: %i)\n", resp_buf->id, fd);
					memcpy((page_mem + (resp_buf->id * page_size)), resp_buf->data, page_size);
					release_exclusive_write(req_buf->id, fd);
					break;
				default:
					fprintf(stderr, "Invalid network command received (cmd: %i, fd: %i). Terminating application.\n", req_buf->net_cmd, fd);
					exit(EXIT_FAILURE);
			}
		}

		close(fd);
		printf("[Thread terminated (fd: %i)]\n", fd);
		pthread_exit(NULL);
}

int main(int argc, char **argv)
	{
		int sock;
		int fd;
		pthread_t tid;

		// Allocate page memory
		if (posix_memalign((void**)&page_mem, page_size, (page_num * page_size)) != 0) {
			fprintf(0, "Error allocating page memory\n");
			exit(EXIT_FAILURE);
		}

		sock = setup_socket(SERVER);
		// Create thread for new connection
		while(1) {
			fd = accept(sock, NULL, 0);
			if (pthread_create(&tid, NULL, server_recv, (void *)(size_t)fd) != 0) {
				perror("pthread_create() failed");
				close(fd);
			}
		}
	}
