#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "sock_setup.h"

//Setup a listening SERVER Socket
//Connect a CLIENT Socket to SERVER Socket
int setup_socket(socket_mode_t mode)
{
	int sock;
	struct sockaddr_in saddr;
	int val = 1;

	saddr.sin_family = AF_INET;
	saddr.sin_addr.s_addr = inet_addr(ADDR);
	saddr.sin_port = htons(PORT);

	// Create socket
	if ((sock = socket(PF_INET, SOCK_STREAM, 0)) == -1) {
		perror("socket() failed");
		exit(EXIT_FAILURE);
	}

	// Socket options: address reuse
	if (setsockopt(sock, SOL_SOCKET,  SO_REUSEADDR, &val, sizeof(val)) != 0) {
		perror("setsockopt() failed");
		exit(EXIT_FAILURE);
	}

	if (mode == SERVER) {
		// Bind socket to address and port
		if (bind(sock, (struct sockaddr *)&saddr, sizeof(saddr)) == -1) {
			perror("bind() failed");
			exit(EXIT_FAILURE);
		}

		// Set socket to listen mode
		if (listen(sock, 0) == -1) {
			perror("listen() failed");
			exit(EXIT_FAILURE);
		}
	} else {
		// Connect socket to destination address and port
		if (connect(sock, (struct sockaddr *)&saddr, sizeof(saddr)) == -1) {
			perror("connect() failed");
			exit(EXIT_FAILURE);
		}
	}

	return sock;
}
