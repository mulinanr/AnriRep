#ifndef __USER_SIGNAL_HANDLER_H
#define __USER_SIGNAL_HANDLER_H

#define REG(x)                  (((struct ucontext *)context)->uc_mcontext.gregs[REG_ ## x])    ///< Direct Regs. access in context of signal handler
#define REG_ERR_RW_BITMASK      (1 << 1)                                                        ///< REG_ERR Bitmask describing r/w bit
#define ACCESS_TYPE             (REG(ERR) & REG_ERR_RW_BITMASK)                                 ///< Type of memory access which caused page fault exception

#define page_size               0x1000                                                          ///< Page size 4096 bytes

typedef enum {
  PAGE_NOACC      = 0x00,
  PAGE_READ       = 0x01,
  PAGE_WRITE      = 0x02
} page_state_t;

unsigned char get_page_state(int id);
void set_page_state(int id, unsigned char state);
int register_segfault_handler(void* addr);

#endif /* __USER_SIGNAL_HANDLER_H */
