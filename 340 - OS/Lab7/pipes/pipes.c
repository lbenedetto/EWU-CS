#include "pipes.h"
#include "../tokenize/makeArgs.h"

#define std_in 0
#define std_out 1
#define child 0

int containsPipe(char *s) {
	int count = 0;
	for (int i = 0; i < strlen(s); i++) {
		if (s[i] == '|') count++;
	}
	printf("containsPipe: %d\n", count);
	return count > 0;
}

char **parsePrePipe(char *s, int *preCount) {
	char **argv = NULL;
	char *temp = calloc(strlen(s) + 1, sizeof(char));
	strcpy(temp, s);
	char *save;
	char *token = strtok_r(temp, "|", &save);
	strip(token);
	*preCount = makeargs(token, &argv);

	printf("prePipe:\n");
	for (int i = 0; i < *preCount; i++) {
		printf("\t%d:%s\n", i, argv[i]);
	}
	free(temp);
	temp = NULL;
	return argv;
}

char **parsePostPipe(char *s, int *postCount) {
	char **argv = NULL;
	char *temp = calloc(strlen(s) + 1, sizeof(char));
	strcpy(temp, s);
	char *save;

	strtok_r(temp, "|", &save);
	char *token = strtok_r(NULL, "|", &save);
	strip(token);
	*postCount = makeargs(token, &argv);

	printf("postPipe:\n");
	for (int i = 0; i < *postCount; i++) {
		printf("\t%d:%s\n", i, argv[i]);
	}
	free(temp);
	temp = NULL;
	return argv;
}

void actuallyPipeIt(char **prePipe, char **postPipe) {
	pid_t pid;
	int fd[2], res, status;

	res = pipe(fd);

	if (res < 0) {
		printf("Pipe Failure\n");
		exit(-1);
	}

	pid = fork();

	if (pid == child) {
		close(fd[std_in]);
		close(std_out);
		dup2(fd[std_out], std_out);
		close(fd[std_out]);
		int result = execvp(prePipe[0], prePipe);
		if (result == -1) { _exit(-99); }
	} else {
		waitpid(pid, &status, 0);
		close(fd[std_out]);
		close(std_in);
		dup2(fd[std_in], std_in);
		close(fd[std_in]);
		int result = execvp(postPipe[0], postPipe);
		if (result == -1) { _exit(-99); }
	}
}

void pipeIt(char **prePipe, char **postPipe) {
	int status;
	pid_t pid = fork();
	if (pid == child) {
		actuallyPipeIt(prePipe, postPipe);
	} else {
		waitpid(pid, &status, 0);
	}
}