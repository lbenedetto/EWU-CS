#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <wait.h>
#include "process.h"

void forkIt(char **argv) {
	int status;
	pid_t pid = fork();
	if(pid != 0){
		waitpid(pid, &status, 0);
	}else{
		char ** parts = (char **) argv[1];
		execvp(argv[0], parts);
	}
}
