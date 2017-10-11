#define _GNU_SOURCE

#include <signal.h>
#include <stdlib.h>
#include <stdio.h>
#include <ucontext.h>
#include <sys/mman.h>

#include "signal_handler.h"
#include "var.h"

#define PAGE_NUMS 3

unsigned char page_state[PAGE_NUMS];
void* page_base;

/**
 * Set page state
 * @param[in] id Page id
 * @param[in] state Page state
 */
void set_page_state(int id, unsigned char state) {
	page_state[id] = state;
}

/**
 * Set page state
 * @param[in] id Page id
 * @return Page state
 */
unsigned char get_page_state(int id) {
	return page_state[id];
}


/**
 * handler for segmentation faults
 * @param[in] signo Signal number
 * @param[in] info Pointer to a structure containing additional information about the raised signal
 * @param[in] context Pointer to a structure holding the user thread context
 */
static void segfault_handler(int signo, siginfo_t *info, struct ucontext *context) {
	void *addr = info->si_addr;
	int write_access = ACCESS_TYPE;
	int id = (size_t)(addr - page_mem) / page_size;

	switch (info->si_code) {
		case SEGV_MAPERR:
			// Handle invalid memory access in non mapped region --> raise SEGV signal
			printf("Invalid memory access at address %p (%s)\n", addr, ((write_access != 0) ? "write" : "read"));
			exit(EXIT_FAILURE);  // do segfault (will never return)
			break;
		
		case SEGV_ACCERR:;
			// Handle read/write fault
			if (write_access) {
				printf("Write fault at address %p (id: %i)\n", addr, id);
				if(page_state[id] == PAGE_NOACC || page_state[id] == PAGE_READ){
					request_page(id, WRITE_REQ);
				}
				page_state[id] = PAGE_WRITE;
				mprotect((page_mem + (id * page_size)), page_size, PROT_WRITE);
			} else {
				printf("Read fault at address %p (id: %i)\n", addr, id);
				if(page_state[id] == PAGE_NOACC || page_state[id] == PAGE_WRITE){
					request_page(id, READ_REQ);
				}
				page_state[id] = PAGE_READ;
				mprotect((page_mem + (id * page_size)), page_size, PROT_READ);
			}
		break;
		
		default:
			/* Handle unknown si_codes */
			printf("Unknown si_code raised\n");
			exit(EXIT_FAILURE);  // do segfault (will never return)
			break;
	}
}


/**
 * Register userspace handler for SEGV signal
 * @retval 0 success
 * @retval -1 error
 */
int register_segfault_handler(void* addr) {
	page_mem = addr;
	
	struct sigaction sigact_struct;
	
	/* Initialize structure to register new handler */
	sigact_struct.sa_handler = NULL;
	sigact_struct.sa_sigaction = (void *)&segfault_handler;
	sigact_struct.sa_flags = SA_SIGINFO;
	if (sigemptyset(&sigact_struct.sa_mask)) return -1;
	
	/* Register signal handler raised on SIGSEGV signals */
	return sigaction(SIGSEGV, &sigact_struct, NULL);
}
