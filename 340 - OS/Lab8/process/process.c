#include "process.h"
#include "../tokenize/makeArgs.h"

void forkIt(char *PATH, char *s) {
	char **argv = NULL;
	int argc = makeargs(s, &argv, " ");
	if (argc != -1) {
		int status;
		pid_t pid = fork();
		if (pid == 0) {
			setenv("PATH", PATH, 1);
			execvp(argv[0], argv);
			fprintf(stderr, "failed to execute %s\n", argv[0]);
			exit(-99);
		} else {
			waitpid(pid, &status, 0);
		}
	}
	clean(argc, argv);
	argv = NULL;
}