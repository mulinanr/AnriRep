#include<stdio.h> //printf
#include<string.h>    //strlen
#include<sys/socket.h>    //socket
#include<arpa/inet.h> //inet_addr

#include<netinet/in.h>
#include<unistd.h>
#include<stdlib.h>
#include<time.h>
#include<sys/mman.h>
#include<pthread.h>

#include "var.h"
#include "client.h"
#include "signal_handler.h"
#include "sock_setup.h"

//Multithreaded Sync
pthread_mutex_t sync_page_req_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t sync_page_req_cond = PTHREAD_COND_INITIALIZER;

int main(int argc, char *argv[]) {
	pthread_t recv;
	int res;

	char str_buf[4096];                               // temporary string buffer

	srand(time(NULL));                                 // initialize random seed
	char rnd_char = 97 + (int) (26.0 * rand() / (RAND_MAX + 1.0)); // generate random char (ASCII: a..z)

	// Allocate access protected page memory
	if (posix_memalign((void**) &page_mem, page_size, (page_num * page_size))
			!= 0) {
		fprintf(0, "Error allocating page memory\n");
		return 1;
	}
	mprotect(page_mem, (page_num * page_size), PROT_NONE);

	// Create socket as recv thread)
	fd = setup_socket(CLIENT);
	res = pthread_create(&recv, NULL, client_recv, (void *) (size_t) fd);
	if (res != 0) {
		perror("pthread_create() failed");
		close(fd);
	}
	if (register_segfault_handler(page_mem) != 0) {
		printf("Cannot register SEGV handler\n");
		return -1;
	}

	// Random client operations
	while (1) {

		client_operations_t operation = (3.0 * rand() / (RAND_MAX + 1.0)); // select random operation (READ, WRITE, SYNC)
		int delay = 100 + (int) (200.0 * rand() / (RAND_MAX + 1.0)); // random delay regarding endless loop (100..300ms)
		size_t page_offset = 0;                 // initialize random page offset


		//page_offset = ((3.0 * rand() / (RAND_MAX + 1.0)) * page_size); // select random shared memory page (used in 'client_a2c' executable only)

		void *addr = page_mem + page_offset;
		printf("addr = %p, delay = %d, operation = %x \n",addr, delay,operation);
		switch (operation) {
		case READ:                      // Read string from first/random memory page
			strncpy(str_buf, addr, STR_LEN);
			printf("[Own char: '%c']: Page content (addr: %p) '%s'\n", rnd_char,
					addr, str_buf);
			break;
		case WRITE:        // Fill first/random memory page with own random char
			memset(addr, rnd_char, page_size);
			break;
		case SYNC: // Send all modified pages back to server (server releases write locks)
			printf("sync\n");
			sync();
			break;
		}
		usleep(delay * 10000); //sleep of 'delay' ms

	}

	return 0;
}
