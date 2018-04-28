#include "process.h"

void forkIt(char *PATH, char **argv) {
	int status;
	pid_t pid = fork();
	if (pid == 0) {
		char pathenv[strlen(PATH) + sizeof("PATH=")];
		sprintf(pathenv, "PATH=%s", PATH);
		char *envp[] = {pathenv, NULL};
		execvpe(argv[0], argv, envp);
		fprintf(stderr, "failed to execute %s\n", argv[0]);
		exit(-99);
	} else {
		waitpid(pid, &status, 0);
	}
}