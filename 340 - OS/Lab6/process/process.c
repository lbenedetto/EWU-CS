#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <wait.h>
#include "process.h"

void forkIt(char **argv) {
	int status;
	pid_t pid = fork();
	if(pid == 0){
		int result = execvp(argv[0], argv);
		if(result == -1) exit(-99);
	}else{
		waitpid(pid, &status, 0);
	}
}
