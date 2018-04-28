#include "process.h"

void forkIt(char *PATH[], char **argv) {
	int status;
	pid_t pid = fork();
	if (pid == 0) {
		//TODO: Put actual path here
		execvpe(argv[0], argv, PATH);
//		execvp(argv[0], argv);
		fprintf(stderr, "failed to execute %s\n", argv[0]);
		exit(-99);
	} else {
		waitpid(pid, &status, 0);
	}
}