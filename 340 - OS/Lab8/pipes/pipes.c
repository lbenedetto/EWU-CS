#include "pipes.h"
#include "stdbool.h"

#define std_in 0
#define std_out 1
#define child 0

//https://stackoverflow.com/questions/916900/having-trouble-with-fork-pipe-dup2-and-exec-in-c/
void pipeIt(char *PATH, char *s, int commandCount) {
	char **commands = NULL;
	int argc = makeargss(s, &commands, "|", commandCount);
	if (argc != -1) {
		pid_t pid;
		int oldFD[2], newFD[2];
		int res, status;
		bool hasPrev = false;
		bool hasNext = false;
		for (int i = 0; i < argc; i++) {
			if ((i + 1) < argc) {
				res = pipe(newFD);
				if (res < 0) {
					printf("Pipe Failure\n");
					exit(-1);
				}
				hasNext = true;
			}
			pid = fork();
			if (pid == child) {
				if (hasPrev) {
					dup2(oldFD[std_in], std_in);
					close(oldFD[std_in]);
					close(oldFD[std_out]);
				}
				if (hasNext) {
					close(newFD[std_in]);
					dup2(newFD[std_out], std_out);
					close(newFD[std_in]);
				}
				char **command;
				makeargs(commands[i], &command, " ");
				char pathenv[strlen(PATH) + sizeof("PATH=")];
				sprintf(pathenv, "PATH=%s", PATH);
				char *envp[] = {pathenv, NULL};
				execvpe(command[0], command, envp);
				fprintf(stderr, "failed to execute %s\n", command[0]);
				exit(-99);
			} else {
				if (hasPrev) {
					close(oldFD[std_in]);
					close(oldFD[std_out]);
				}
				if (hasNext) {
					oldFD[std_in] = newFD[std_in];
					oldFD[std_out] = newFD[std_out];
				}
			}
			waitpid(pid, &status, 0);
			hasNext = false;
			hasPrev = true;
		}
		if (argc > 1) {
			close(oldFD[std_in]);
			close(oldFD[std_out]);
		}
	}
	clean(argc, commands);
	commands = NULL;
}