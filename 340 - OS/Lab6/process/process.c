#include "process.h"
#include <unistd.h>
#include <wait.h>

void forkIt(char **argv) {
	int status;
	pid_t pid = fork();
	if(pid == 0){
		int result = execvp(argv[0], argv);
		exit(result);
	}else{
		waitpid(pid, &status, 0);
	}
}
